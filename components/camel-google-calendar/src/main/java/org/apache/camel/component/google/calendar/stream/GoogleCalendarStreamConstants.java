begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.google.calendar.stream
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|google
operator|.
name|calendar
operator|.
name|stream
package|;
end_package

begin_comment
comment|/**  * Constants used in Camel Google Calendar Stream  */
end_comment

begin_interface
DECL|interface|GoogleCalendarStreamConstants
specifier|public
interface|interface
name|GoogleCalendarStreamConstants
block|{
DECL|field|MAIL_TO
name|String
name|MAIL_TO
init|=
literal|"CamelGoogleMailStreamTo"
decl_stmt|;
DECL|field|MAIL_FROM
name|String
name|MAIL_FROM
init|=
literal|"CamelGoogleMailStreamFrom"
decl_stmt|;
DECL|field|MAIL_CC
name|String
name|MAIL_CC
init|=
literal|"CamelGoogleMailStreamCc"
decl_stmt|;
DECL|field|MAIL_BCC
name|String
name|MAIL_BCC
init|=
literal|"CamelGoogleMailStreamBcc"
decl_stmt|;
DECL|field|MAIL_SUBJECT
name|String
name|MAIL_SUBJECT
init|=
literal|"CamelGoogleMailStreamSubject"
decl_stmt|;
DECL|field|MAIL_ID
name|String
name|MAIL_ID
init|=
literal|"CamelGoogleMailId"
decl_stmt|;
block|}
end_interface

end_unit

