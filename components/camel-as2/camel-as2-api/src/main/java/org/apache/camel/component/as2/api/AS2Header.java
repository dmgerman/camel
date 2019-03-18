begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.as2.api
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|as2
operator|.
name|api
package|;
end_package

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|protocol
operator|.
name|HTTP
import|;
end_import

begin_interface
DECL|interface|AS2Header
specifier|public
interface|interface
name|AS2Header
block|{
comment|/**      * Message Header Name for MIME Version      */
DECL|field|MIME_VERSION
specifier|public
specifier|static
specifier|final
name|String
name|MIME_VERSION
init|=
literal|"MIME-Version"
decl_stmt|;
comment|/**      * Message Header Name for AS2 From      */
DECL|field|AS2_FROM
specifier|public
specifier|static
specifier|final
name|String
name|AS2_FROM
init|=
literal|"AS2-From"
decl_stmt|;
comment|/**      * Message Header Name for AS2 Version      */
DECL|field|AS2_VERSION
specifier|public
specifier|static
specifier|final
name|String
name|AS2_VERSION
init|=
literal|"AS2-Version"
decl_stmt|;
comment|/**      * Message Header Name for Content Type      */
DECL|field|CONTENT_TYPE
specifier|public
specifier|static
specifier|final
name|String
name|CONTENT_TYPE
init|=
literal|"Content-Type"
decl_stmt|;
comment|/**      * Message Header Name for AS2 To      */
DECL|field|AS2_TO
specifier|public
specifier|static
specifier|final
name|String
name|AS2_TO
init|=
literal|"AS2-To"
decl_stmt|;
comment|/**      * Message Header Name for From      */
DECL|field|FROM
specifier|public
specifier|static
specifier|final
name|String
name|FROM
init|=
literal|"From"
decl_stmt|;
comment|/**      * Message Header Name for Subject      */
DECL|field|SUBJECT
specifier|public
specifier|static
specifier|final
name|String
name|SUBJECT
init|=
literal|"Subject"
decl_stmt|;
comment|/**      * Message Header Name for Message ID      */
DECL|field|MESSAGE_ID
specifier|public
specifier|static
specifier|final
name|String
name|MESSAGE_ID
init|=
literal|"Message-Id"
decl_stmt|;
comment|/**      * Message Header Name for Target Host      */
DECL|field|TARGET_HOST
specifier|public
specifier|static
specifier|final
name|String
name|TARGET_HOST
init|=
name|HTTP
operator|.
name|TARGET_HOST
decl_stmt|;
comment|/**      * Message Header Name for User Agent      */
DECL|field|USER_AGENT
specifier|public
specifier|static
specifier|final
name|String
name|USER_AGENT
init|=
name|HTTP
operator|.
name|USER_AGENT
decl_stmt|;
comment|/**      * Message Header Name for Server Name      */
DECL|field|SERVER
specifier|public
specifier|static
specifier|final
name|String
name|SERVER
init|=
name|HTTP
operator|.
name|SERVER_HEADER
decl_stmt|;
comment|/**      * Message Header Name for Date      */
DECL|field|DATE
specifier|public
specifier|static
specifier|final
name|String
name|DATE
init|=
name|HTTP
operator|.
name|DATE_HEADER
decl_stmt|;
comment|/**      * Message Header Name for Content Length      */
DECL|field|CONTENT_LENGTH
specifier|public
specifier|static
specifier|final
name|String
name|CONTENT_LENGTH
init|=
name|HTTP
operator|.
name|CONTENT_LEN
decl_stmt|;
comment|/**      * Message Header Name for Connection      */
DECL|field|CONNECTION
specifier|public
specifier|static
specifier|final
name|String
name|CONNECTION
init|=
name|HTTP
operator|.
name|CONN_DIRECTIVE
decl_stmt|;
comment|/**      * Message Header Name for Expect      */
DECL|field|EXPECT
specifier|public
specifier|static
specifier|final
name|String
name|EXPECT
init|=
name|HTTP
operator|.
name|EXPECT_DIRECTIVE
decl_stmt|;
comment|/**      * Message Header name for Content Transfer Encoding      */
DECL|field|CONTENT_TRANSFER_ENCODING
specifier|public
specifier|static
specifier|final
name|String
name|CONTENT_TRANSFER_ENCODING
init|=
literal|"Content-Transfer-Encoding"
decl_stmt|;
comment|/**      * Message Header name for Content Disposition      */
DECL|field|CONTENT_DISPOSITION
specifier|public
specifier|static
specifier|final
name|String
name|CONTENT_DISPOSITION
init|=
literal|"Content-Disposition"
decl_stmt|;
comment|/**      * Message Header name for Content Description      */
DECL|field|CONTENT_DESCRIPTION
specifier|public
specifier|static
specifier|final
name|String
name|CONTENT_DESCRIPTION
init|=
literal|"Content-Description"
decl_stmt|;
comment|/**      * Message Header name for Disposition Notification To      */
DECL|field|DISPOSITION_NOTIFICATION_TO
specifier|public
specifier|static
specifier|final
name|String
name|DISPOSITION_NOTIFICATION_TO
init|=
literal|"Disposition-Notification-To"
decl_stmt|;
comment|/**      * Message Header name for Receipt Delivery Option      */
DECL|field|RECEIPT_DELIVERY_OPTION
specifier|public
specifier|static
specifier|final
name|String
name|RECEIPT_DELIVERY_OPTION
init|=
literal|"Receipt-Delivery-Option"
decl_stmt|;
comment|/**      * Message Header name for Receipt Address      */
DECL|field|RECIPIENT_ADDRESS
specifier|public
specifier|static
specifier|final
name|String
name|RECIPIENT_ADDRESS
init|=
literal|"Recipient-Address"
decl_stmt|;
comment|/**      * Message Header name for Disposition Notification Options      */
DECL|field|DISPOSITION_NOTIFICATION_OPTIONS
specifier|public
specifier|static
specifier|final
name|String
name|DISPOSITION_NOTIFICATION_OPTIONS
init|=
literal|"Disposition-Notification-Options"
decl_stmt|;
block|}
end_interface

end_unit

