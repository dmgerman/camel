begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.cm
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|cm
package|;
end_package

begin_interface
DECL|interface|CMConstants
specifier|public
interface|interface
name|CMConstants
block|{
DECL|field|DEFAULT_SCHEME
name|String
name|DEFAULT_SCHEME
init|=
literal|"https://"
decl_stmt|;
DECL|field|DEFAULT_MULTIPARTS
name|int
name|DEFAULT_MULTIPARTS
init|=
literal|8
decl_stmt|;
DECL|field|MAX_UNICODE_MESSAGE_LENGTH
name|int
name|MAX_UNICODE_MESSAGE_LENGTH
init|=
literal|70
decl_stmt|;
DECL|field|MAX_GSM_MESSAGE_LENGTH
name|int
name|MAX_GSM_MESSAGE_LENGTH
init|=
literal|160
decl_stmt|;
DECL|field|MAX_UNICODE_MESSAGE_LENGTH_PER_PART_IF_MULTIPART
name|int
name|MAX_UNICODE_MESSAGE_LENGTH_PER_PART_IF_MULTIPART
init|=
literal|67
decl_stmt|;
DECL|field|MAX_GSM_MESSAGE_LENGTH_PER_PART_IF_MULTIPART
name|int
name|MAX_GSM_MESSAGE_LENGTH_PER_PART_IF_MULTIPART
init|=
literal|153
decl_stmt|;
comment|// status code 200 - Error substrings - check it contains.
DECL|field|ERROR_UNKNOWN
name|String
name|ERROR_UNKNOWN
init|=
literal|"Unknown error"
decl_stmt|;
DECL|field|ERROR_NO_ACCOUNT
name|String
name|ERROR_NO_ACCOUNT
init|=
literal|"No account found"
decl_stmt|;
DECL|field|ERROR_INSUFICIENT_BALANCE
name|String
name|ERROR_INSUFICIENT_BALANCE
init|=
literal|"Insufficient balance"
decl_stmt|;
DECL|field|ERROR_UNROUTABLE_MESSAGE
name|String
name|ERROR_UNROUTABLE_MESSAGE
init|=
literal|"Message is unroutable"
decl_stmt|;
DECL|field|ERROR_INVALID_PRODUCT_TOKEN
name|String
name|ERROR_INVALID_PRODUCT_TOKEN
init|=
literal|"Invalid product token"
decl_stmt|;
comment|// TODO: Review this pattern.
comment|// or it should be foundnd an alternative to jcharset to check if a message is GSM 03.38 encodable
comment|// See:
comment|// https://en.wikipedia.org/wiki/GSM_03.38
comment|// http://frightanic.com/software-development/regex-for-gsm-03-38-7bit-character-set/
DECL|field|GSM_0338_REGEX
name|String
name|GSM_0338_REGEX
init|=
literal|"^[A-Za-z0-9 \\r\\n@Â£$Â¥Ã¨Ã©Ã¹Ã¬Ã²ÃÃÃ¸ÃÃ¥\u0394_\u03A6\u0393\u039B\u03A9\u03A0\u03A8\u03A3\u0398\u039EÃÃ¦ÃÃ!\"#$%&amp;'()*+,\\-./:;&lt;=&gt;?Â¡ÃÃÃÃÂ§Â¿Ã¤Ã¶Ã±Ã¼Ã ^{}\\\\\\[~\\]|\u20AC]*$"
decl_stmt|;
block|}
end_interface

end_unit

