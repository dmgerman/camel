begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.sip.listener
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|sip
operator|.
name|listener
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_interface
DECL|interface|SipMessageCodes
specifier|public
interface|interface
name|SipMessageCodes
block|{
DECL|field|SIP_MESSAGE_CODES
name|Map
argument_list|<
name|Integer
argument_list|,
name|String
argument_list|>
name|SIP_MESSAGE_CODES
init|=
operator|new
name|HashMap
argument_list|<
name|Integer
argument_list|,
name|String
argument_list|>
argument_list|()
block|{
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|1L
decl_stmt|;
block|{
name|put
argument_list|(
literal|100
argument_list|,
literal|"Trying"
argument_list|)
expr_stmt|;
name|put
argument_list|(
literal|180
argument_list|,
literal|"Ringing"
argument_list|)
expr_stmt|;
name|put
argument_list|(
literal|181
argument_list|,
literal|"Call Being Forwarded"
argument_list|)
expr_stmt|;
name|put
argument_list|(
literal|182
argument_list|,
literal|"Call Queued"
argument_list|)
expr_stmt|;
name|put
argument_list|(
literal|183
argument_list|,
literal|"Session Progress"
argument_list|)
expr_stmt|;
name|put
argument_list|(
literal|200
argument_list|,
literal|"OK"
argument_list|)
expr_stmt|;
name|put
argument_list|(
literal|202
argument_list|,
literal|"Accepted"
argument_list|)
expr_stmt|;
name|put
argument_list|(
literal|300
argument_list|,
literal|"Multiple Choices"
argument_list|)
expr_stmt|;
name|put
argument_list|(
literal|301
argument_list|,
literal|"Moved Permanently"
argument_list|)
expr_stmt|;
name|put
argument_list|(
literal|302
argument_list|,
literal|"Moved Temporarily"
argument_list|)
expr_stmt|;
name|put
argument_list|(
literal|305
argument_list|,
literal|"Use Proxy"
argument_list|)
expr_stmt|;
name|put
argument_list|(
literal|380
argument_list|,
literal|"Alternative Service"
argument_list|)
expr_stmt|;
name|put
argument_list|(
literal|400
argument_list|,
literal|"Bad Request"
argument_list|)
expr_stmt|;
name|put
argument_list|(
literal|401
argument_list|,
literal|"Unauthorized"
argument_list|)
expr_stmt|;
name|put
argument_list|(
literal|402
argument_list|,
literal|"Payment Required"
argument_list|)
expr_stmt|;
name|put
argument_list|(
literal|403
argument_list|,
literal|"Forbidden"
argument_list|)
expr_stmt|;
name|put
argument_list|(
literal|404
argument_list|,
literal|"Not Found"
argument_list|)
expr_stmt|;
name|put
argument_list|(
literal|405
argument_list|,
literal|"Method Not Allowed"
argument_list|)
expr_stmt|;
name|put
argument_list|(
literal|406
argument_list|,
literal|"Not Acceptable"
argument_list|)
expr_stmt|;
name|put
argument_list|(
literal|407
argument_list|,
literal|"Proxy Authentication Required"
argument_list|)
expr_stmt|;
name|put
argument_list|(
literal|408
argument_list|,
literal|"Request Timeout"
argument_list|)
expr_stmt|;
name|put
argument_list|(
literal|409
argument_list|,
literal|"Conflict"
argument_list|)
expr_stmt|;
name|put
argument_list|(
literal|410
argument_list|,
literal|"Gone"
argument_list|)
expr_stmt|;
name|put
argument_list|(
literal|411
argument_list|,
literal|"Length Required"
argument_list|)
expr_stmt|;
name|put
argument_list|(
literal|413
argument_list|,
literal|"Request Entity Too Large"
argument_list|)
expr_stmt|;
name|put
argument_list|(
literal|414
argument_list|,
literal|"Request URI Too Long"
argument_list|)
expr_stmt|;
name|put
argument_list|(
literal|415
argument_list|,
literal|"Unsupported Media Type"
argument_list|)
expr_stmt|;
name|put
argument_list|(
literal|416
argument_list|,
literal|"Unsupported URI Scheme"
argument_list|)
expr_stmt|;
name|put
argument_list|(
literal|420
argument_list|,
literal|"Bad Extension"
argument_list|)
expr_stmt|;
name|put
argument_list|(
literal|421
argument_list|,
literal|"Extension Required"
argument_list|)
expr_stmt|;
name|put
argument_list|(
literal|423
argument_list|,
literal|"Interval Too Brief"
argument_list|)
expr_stmt|;
name|put
argument_list|(
literal|480
argument_list|,
literal|"Temporarily Unavailable"
argument_list|)
expr_stmt|;
name|put
argument_list|(
literal|481
argument_list|,
literal|"Call/Transaction Does Not Exist"
argument_list|)
expr_stmt|;
name|put
argument_list|(
literal|482
argument_list|,
literal|"Loop Detected"
argument_list|)
expr_stmt|;
name|put
argument_list|(
literal|483
argument_list|,
literal|"Too Many Hops"
argument_list|)
expr_stmt|;
name|put
argument_list|(
literal|484
argument_list|,
literal|"Address Incomplete"
argument_list|)
expr_stmt|;
name|put
argument_list|(
literal|485
argument_list|,
literal|"Ambiguous"
argument_list|)
expr_stmt|;
name|put
argument_list|(
literal|486
argument_list|,
literal|"Busy Here"
argument_list|)
expr_stmt|;
name|put
argument_list|(
literal|487
argument_list|,
literal|"Request Terminated"
argument_list|)
expr_stmt|;
name|put
argument_list|(
literal|488
argument_list|,
literal|"Not Acceptable Here"
argument_list|)
expr_stmt|;
name|put
argument_list|(
literal|491
argument_list|,
literal|"Request Pending"
argument_list|)
expr_stmt|;
name|put
argument_list|(
literal|493
argument_list|,
literal|"Undecipherable"
argument_list|)
expr_stmt|;
name|put
argument_list|(
literal|500
argument_list|,
literal|"Server Internal Error"
argument_list|)
expr_stmt|;
name|put
argument_list|(
literal|501
argument_list|,
literal|"Not Implemented"
argument_list|)
expr_stmt|;
name|put
argument_list|(
literal|502
argument_list|,
literal|"Bad Gateway"
argument_list|)
expr_stmt|;
name|put
argument_list|(
literal|503
argument_list|,
literal|"Service Unavailable"
argument_list|)
expr_stmt|;
name|put
argument_list|(
literal|504
argument_list|,
literal|"Server Time-Out"
argument_list|)
expr_stmt|;
name|put
argument_list|(
literal|505
argument_list|,
literal|"Version Not Supported"
argument_list|)
expr_stmt|;
name|put
argument_list|(
literal|513
argument_list|,
literal|"Message Too Large"
argument_list|)
expr_stmt|;
name|put
argument_list|(
literal|600
argument_list|,
literal|"Busy Everywhere"
argument_list|)
expr_stmt|;
name|put
argument_list|(
literal|603
argument_list|,
literal|"Declined"
argument_list|)
expr_stmt|;
name|put
argument_list|(
literal|604
argument_list|,
literal|"Does Not Exist Anywhere"
argument_list|)
expr_stmt|;
name|put
argument_list|(
literal|605
argument_list|,
literal|"Not Acceptable"
argument_list|)
expr_stmt|;
block|}
block|}
decl_stmt|;
block|}
end_interface

end_unit

