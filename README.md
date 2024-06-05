# Infrared passenger flow series product communication protocol

<font color="red">NEW：Added a new recording period field</font>

## 1、Protocol Description

- The agreement is currently only applicable to the analog version series of infrared passenger flow products, and other products will support this agreement in the future. Please keep an eye on our warehouse for future updates.
- When designing this protocol, low power consumption, low computing power, and other devices were also considered, so the protocol is directly based on TCP and uses hexadecimal+ANSI code for transmission
- The protocol takes into account special situations such as packet sticking and escaping

## 2、Protocol Explanation

### 2.1 Protocol structure 

 The protocol consists of 8 parts: fixed header, fixed footer, device ID, instruction, parameters, data length, data, CRC, and frame  footer  

 The fixed header and fixed tail are both fixed, and the device ID is  currently a reserved bit. It defaults to a 32-bit integer and is  currently fixed to 0xFF 0xFF 0xFF 0xFF. CRC uses ModbusCRC16 method, and the data part is divided into fields using commas. In order to prevent fixed header and fixed footer data from appearing  in the data section and CRC section, the escape bit: 0x7C has been  introduced.  

| Fixed frame header | Fixed device ID | Command | Parameter | Data length |       Data        | ModbusCRC16 Lower eight bits | ModbusCRC16High eight bits | Fixed frame end |
| :----------------: | :-------------: | :-----: | :-------: | :---------: | :---------------: | :--------------------------: | :------------------------: | :-------------: |
|       1 byte       |     4 bytes     | 1 byte  |  1 byte   | Double Byte | Indefinite length |            1 byte            |           1 byte           |     1 byte      |

- -Fixed frame header: 0x7E

  -Fixed device ID: 0xFF 0xFF 0xFF 0xFF

  -Instructions: Please refer to the instruction description section for details

  -Parameters: Please refer to the instruction description section for details

  -Data length: The length of the data portion, type int16, is transmitted in doublewords

  -Data: ANSI strings

  -ModbusCRC16 Low Octet: ModbusCRC16 Low Octet

  -ModbusCRC16 high octets: ModbusCRC16 high octets

  -Fixed frame tail: 0x7D

  -Escape character: 0x7C

#### 2.1.1 Fixed header and footer

The header is 7E and ends at 7D.

7E ............Data like 7D is a complete package of data

#### 2.1.2 Instructions and Parameters

Instructions and parameters are used to distinguish different data types, and the latest version only **reports data**

#### 2.1.3 Data length and data

 The data length is used to calculate the length of the data section and to  facilitate developers in truncating the data section through the data  length. The data in the data section is composed of **field positions and  comma separators**. Detailed explanations will be provided in the field definition

Example：

Client initiated data：1902201010001,20220101090004,10,20,1,V4.3,E20          注：长度为45

Actual data initiated by the client：00 2D 31 39 30 32 32 30 31 30 31 30 30 30 31 2C 32 30 32 32 30 31 30 31 30 39 30 30 30 34 2C 31 30 2C 32 30 2C 31 2C 56 34 2E 33 2C 45 32 30

Note: The first two bytes are the length of the data：0x00 0x2D。High position in front, low position in the back. The following is the ASCII code corresponding to the st

#### 2.1.4 ModbusCRC16 verification

ModbusCRC16 verification range does not include fixed frame headers and fixed frame endings. The calculation range includes fixed device ID, instructions, parameters, data length, and data。These positions must participate in ModbusCRC16 calculations. The purpose of ModbusCRC16 verification calculation is to ensure the accuracy of data. The C language implementation code is as follows：

```c
#include "h_crc.h"

ushort const TAB_CRC16_ITU[256] =
    {
        0x0000, 0xC0C1, 0xC181, 0x0140, 0xC301, 0x03C0, 0x0280, 0xC241,
        0xC601, 0x06C0, 0x0780, 0xC741, 0x0500, 0xC5C1, 0xC481, 0x0440,
        0xCC01, 0x0CC0, 0x0D80, 0xCD41, 0x0F00, 0xCFC1, 0xCE81, 0x0E40,
        0x0A00, 0xCAC1, 0xCB81, 0x0B40, 0xC901, 0x09C0, 0x0880, 0xC841,
        0xD801, 0x18C0, 0x1980, 0xD941, 0x1B00, 0xDBC1, 0xDA81, 0x1A40,
        0x1E00, 0xDEC1, 0xDF81, 0x1F40, 0xDD01, 0x1DC0, 0x1C80, 0xDC41,
        0x1400, 0xD4C1, 0xD581, 0x1540, 0xD701, 0x17C0, 0x1680, 0xD641,
        0xD201, 0x12C0, 0x1380, 0xD341, 0x1100, 0xD1C1, 0xD081, 0x1040,
        0xF001, 0x30C0, 0x3180, 0xF141, 0x3300, 0xF3C1, 0xF281, 0x3240,
        0x3600, 0xF6C1, 0xF781, 0x3740, 0xF501, 0x35C0, 0x3480, 0xF441,
        0x3C00, 0xFCC1, 0xFD81, 0x3D40, 0xFF01, 0x3FC0, 0x3E80, 0xFE41,
        0xFA01, 0x3AC0, 0x3B80, 0xFB41, 0x3900, 0xF9C1, 0xF881, 0x3840,
        0x2800, 0xE8C1, 0xE981, 0x2940, 0xEB01, 0x2BC0, 0x2A80, 0xEA41,
        0xEE01, 0x2EC0, 0x2F80, 0xEF41, 0x2D00, 0xEDC1, 0xEC81, 0x2C40,
        0xE401, 0x24C0, 0x2580, 0xE541, 0x2700, 0xE7C1, 0xE681, 0x2640,
        0x2200, 0xE2C1, 0xE381, 0x2340, 0xE101, 0x21C0, 0x2080, 0xE041,
        0xA001, 0x60C0, 0x6180, 0xA141, 0x6300, 0xA3C1, 0xA281, 0x6240,
        0x6600, 0xA6C1, 0xA781, 0x6740, 0xA501, 0x65C0, 0x6480, 0xA441,
        0x6C00, 0xACC1, 0xAD81, 0x6D40, 0xAF01, 0x6FC0, 0x6E80, 0xAE41,
        0xAA01, 0x6AC0, 0x6B80, 0xAB41, 0x6900, 0xA9C1, 0xA881, 0x6840,
        0x7800, 0xB8C1, 0xB981, 0x7940, 0xBB01, 0x7BC0, 0x7A80, 0xBA41,
        0xBE01, 0x7EC0, 0x7F80, 0xBF41, 0x7D00, 0xBDC1, 0xBC81, 0x7C40,
        0xB401, 0x74C0, 0x7580, 0xB541, 0x7700, 0xB7C1, 0xB681, 0x7640,
        0x7200, 0xB2C1, 0xB381, 0x7340, 0xB101, 0x71C0, 0x7080, 0xB041,
        0x5000, 0x90C1, 0x9181, 0x5140, 0x9301, 0x53C0, 0x5280, 0x9241,
        0x9601, 0x56C0, 0x5780, 0x9741, 0x5500, 0x95C1, 0x9481, 0x5440,
        0x9C01, 0x5CC0, 0x5D80, 0x9D41, 0x5F00, 0x9FC1, 0x9E81, 0x5E40,
        0x5A00, 0x9AC1, 0x9B81, 0x5B40, 0x9901, 0x59C0, 0x5880, 0x9841,
        0x8801, 0x48C0, 0x4980, 0x8941, 0x4B00, 0x8BC1, 0x8A81, 0x4A40,
        0x4E00, 0x8EC1, 0x8F81, 0x4F40, 0x8D01, 0x4DC0, 0x4C80, 0x8C41,
        0x4400, 0x84C1, 0x8581, 0x4540, 0x8701, 0x47C0, 0x4680, 0x8641,
        0x8201, 0x42C0, 0x4380, 0x8341, 0x4100, 0x81C1, 0x8081, 0x4040};

Bool Is_CRC16_Good(uchar *pData, uint nLength)
{
    ushort Fcs = 0xFFFF;

    while (nLength > 0)
    {
        Fcs = (Fcs >> 8) ^ TAB_CRC16_ITU[(Fcs ^ *pData) & 0xFF];
        nLength--;
        pData++;
    }

    if (Fcs == 0)
        return True;
    else
        return False;
}

ushort CRC16_ITU_Generate(uchar *pData, int nLength)
{
    ushort Fcs;

    Fcs = 0xFFFF;
    while (nLength > 0)
    {
        Fcs = (Fcs >> 8) ^ TAB_CRC16_ITU[(Fcs ^ *pData) & 0xFF];
        nLength--;
        pData++;
    }

    return Fcs;
}

```

When verifying data verification, it is necessary to perform verification calculation again by combining the fixed device ID, instruction, parameter, data length, data, and ModbusCRC16 bytes. If the result is equal to 0, it indicates that the verification is successful; otherwise, it indicates a failure

#### 2.1.5 Escape 

The purpose of escape characters is to prevent non header and footer positions from appearing when the sender sends a message, which makes it difficult for the receiving method to parse the data. The specific implementation methods are as follows：

0x7D：Change 0x7C 0x5D send 

0x7E：Change 0x7C 0x5E send 

It is worth noting here that what should be done if 7C itself appears. Similarly:

0x7C: changes to 0x7C 0x5C and sends

The specific implementation code is as follows: 

Sender:

```c
case 0x7D: 
	rsbus_frame_data[rsbus_frame_data_index++] = 0x7C;
	rsbus_frame_data[rsbus_frame_data_index++] = rsbus_send.send_buf[i] - 0x20;
	break;
case 0x7E: 
	rsbus_frame_data[rsbus_frame_data_index++] = 0x7C;
	rsbus_frame_data[rsbus_frame_data_index++] = rsbus_send.send_buf[i] - 0x20;
	break;
case 0x7C: 
	rsbus_frame_data[rsbus_frame_data_index++] = 0x7C;
	rsbus_frame_data[rsbus_frame_data_index++] = rsbus_send.send_buf[i] - 0x20;
	break;
```

When escaping, the sender first sends 0x7C, and then subtracts 0x20. 

Receiver:

```c#
if (recv == 0x7C) 
{
	if (rec_head_flag == true) 
	{
		rec_specialData_flag = true; 
        return;
	}
}

if (rec_specialData_flag == true) 
{
    recbuf[recbuf_index++] = (byte)(recv ^ 0x20);
    rec_specialData_flag = false;

    return;
}
```

When the receiver receives 7C, it needs to restore it. The specific method is to perform ^ 0x20 operation on the data after 0x7C to restore it.

### 2.2 Protocol instruction description

The data bit is transmitted in hexadecimal format using ANSII code. For the convenience of demonstration, the string

#### 2.2.1Heartbeat command 0xD1 <font color="red">Note: The new version of the device will not report 0xD1</font>

-  Heartbeat data instruction format of the sender 

Instruction: 0xD1 

Parameter: default 0x02 

Data part: [SN], [timestamp], [received power], [send power], [binding status], [version], [product model] ,: comma, separator, used to split data

SN: 13-bit serial number, string, may not have a fixed length 

Timestamp: current 14-bit timestamp, example: 20220101090004 

Received power: 1 means normal power, 0 means warning power 

Send power: 1 means normal power, 0 means warning power 

Bind status: The transmitter works normally. If the binding is normal, this field is 1. If the binding is not successful, this field is 0 

Version: device software version number, not fixed length, such as: V4.1 V1.0.1 

Product model: device model, the same product is a fixed value, such as: I Series

The receiver's heartbeat data replies to the sender's command 

Command: 0xD1 Parameter: default 0x02 Data part: [status code], [timestamp], [business start time], [business end time], [upload interval], [[recording cycle], [statistical direction], [upgrade flag], [upgrade link], [reserved] ,: comma, separator, used to split data 

Status code: 0 success 1 failure Timestamp: The device synchronization time in the receiving direction. After the device receives it, it will be consistent with the receiver's time and start to count from the receiver's time again. Example: 20220101090004 

Business start time: The start time of the device every day. The length must be fixed and in 24-hour format. The units are only hours and minutes, for example: 0800, 0830 

Business end time: The end time of the device every day. The length must be fixed and in 24-hour format. Units are only hours and minutes, for example: 2230, 2359 

Recording cycle: the time the device saves data, currently in minutes, cannot be set arbitrarily, subject to the attached table 

Upload interval: that is, the upload cycle, currently in minutes. Cannot be set arbitrarily, subject to the attached table 

Statistical direction: the statistical calculation direction of the device, the default is 0 bidirectional, 1 only input, 2 only output 

Upgrade flag: 0 means no upgrade, 1 means upgrading the device firmware, 2 means upgrading the WIFI firmware. If the device needs to be upgraded

Upgrade link: If there is an upgrade, then the upgrade link is the actual firmware download link, such as: http://dddddd.com/file.bin Note: Download link does not support https 

Reserved: Reserved item, write 0

#### 2.2.2 Statistics instruction 0xD2 <font color="red"> Note: The new version of the device status report uses 0xD2. If the status is reported, the in and out will be 0.</font>

- Sender statistics command format

Instruction: 0xD2 

Parameter: default 0x02 

Data part: [SN], [timestamp], [number of people entering], [number of people leaving], [receiving power], [transmitting power], [binding status], [version], [product model] ,: comma, separator, used to split data 

SN: 13-bit serial number, string, may not have a fixed length Timestamp: current 14-bit timestamp, example: 20220101090004 

Number of people entering: total number of people entering during the recording period 

Number of people leaving: total number of people leaving during the recording period Receiving power: 1 indicates normal power, 0 indicates warning power

Transmitting power: 1 indicates normal power, 0 indicates warning power 

Binding status: the transmitter works normally. If the binding is normal, this field is 1. If the binding is not successful, this field is 0 

Version: device software version number, not fixed length, such as: V4.1, V1.0.1 

Product model: device model, not fixed length, such as: I Series

- The receiving party's statistics reply to the sending party's instructions

Command: 0xD2 

Parameter: Default 0x02 Data part: [Status code], [Timestamp], [Business start time], [Business end time], [Upload interval], [Record cycle], [Statistical direction], [Upgrade flag], [Upgrade link], [Reserved] ,: Comma, separator, used to split data Status code: 0 Success 1 Failure 

Timestamp: The device synchronization time in the receiving direction. After the device receives it, it will be consistent with the receiver's time and start to count from the receiver's time. Example: 20220101090004 

Business start time: The start time of the device every day. The length must be fixed and in 24-hour format. The units are only hours and minutes, for example: 0800, 0830 

Business end time: The end time of the device every day. The length must be fixed and in 24-hour format. Units are only hours and minutes, for example: 2230, 2359 

Recording cycle: the time the device saves data, currently in minutes, cannot be set arbitrarily, subject to the attached table 

Upload interval: that is, the upload cycle, currently in minutes. Cannot be set arbitrarily, subject to the attached table 

Statistical direction: the statistical calculation direction of the device, the default is 0 bidirectional, 1 only input, 2 only output Upgrade flag: 0 means no upgrade, 1 means upgrading the device firmware, 2 means upgrading the WIFI firmware. 

Upgrade link: If there is an upgrade, then the upgrade link is the actual firmware download link, such as: http://dddddd.com/file.bin Note: Download link does not support https 

Reserved: Reserved item, write 0

## 3、Send and receive interaction example

​     ***Device startup***

​        &dArr;

 Device initiated 0xD2, in and out 0, synchronization time, platform reply timestamp, business hours 0:00 to 23:59, 5 minutes reporting interval, 1 minute recording cycle. Two-way statistics, no upgrade

HEX：7E FF FF FF FF D2 02 00 35 31 39 35 32 33 30 37 32 38 30 32 36 39 2C 32 30 32 33 30 31 30 31 30 39 30 30 30 30 2C 30 2C 30 2C 31 2C 31 2C 31 2C 56 31 2E 30 2D 45 4E 2C 48 58 2D 4E 42 49 89 D3 7D

Data section：1952307280269,20230101090000,0,0,1,1,1,V1.0-EN,HX-NBI

Platform response：7E FF FF FF FF D2 02 00 26 30 2C 32 30 32 34 30 31 32 34 31 38 34 31 31 35 2C 30 30 30 30 2C 32 33 35 39 2C 35 2C 31 2C 30 2C 30 2C 30 2C 70 46 30 22 F8 7D

Data section：0,20240124184115,0000,2359,5,1,0,0,0,0   

​      &dArr;

The device initiated statistics, 10 in, 8 out, the receiving and sending power are normal. Code pairing is normal

HEX：7E FF FF FF FF D2 02 00 36 31 39 35 32 33 30 37 32 38 30 32 36 39 2C 32 30 32 33 30 31 30 31 30 39 30 30 30 30 2C 31 30 2C 38 2C 31 2C 31 2C 31 2C 56 31 2E 30 2D 45 4E 2C 48 58 2D 4E 42 49 14 00 7D

Data section：1952307280269,20230101090000,10,8,1,1,1,V1.0-EN,HX-NBI

Platform response：7E FF FF FF FF D2 D2 00 27 30 2C 32 30 32 34 30 31 32 34 31 38 34 31 31 35 2C 30 30 30 30 2C 32 33 35 39 2C 35 2C 31 2C 30 2C 30 2C 30 2C 30 20 5D C9 7D

Data section：0,20240124184115,0000,2359,5,1,0,0,0,0 



## 4、Interaction logic diagram between platform devices and devices



![image-20240328110839359](C:\Users\002\AppData\Roaming\Typora\typora-user-images\image-20240328110839359.png)



## 5、Recommended value table for recording cycle and reporting interval

- The reporting interval must be a multiple of the recording period
- The minimum recording period is 1 minute and the maximum is 5 minutes
- The minimum reporting interval is 1 minute and the maximum is 60 minutes.
- The factory default recording period is 5 minutes and the reporting interval is 60 minutes.
- The longer the recording period, the longer the reporting interval, and the longer the battery life. The reporting interval has the greatest impact on battery life.
- The shorter the recording period is, the finer the platform query dimension will be. 5 minutes is highly recommended.

Recommended value

| Recording period (minutes) | Reporting interval (minutes) | Number of days to store failed data (24 hours) |
| :------------------------: | :--------------------------: | :--------------------------------------------: |
|             1              |              5               |                      1.5                       |
|             1              |              10              |                      1.5                       |
|             5              |              5               |                      7.5                       |
|             5              |              10              |                      7.5                       |
|             5              |              30              |                      7.5                       |
|             5              |              60              |                      7.5                       |

