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
name|HttpCoreContext
import|;
end_import

begin_comment
comment|/**  * Constants for AS2 component.  */
end_comment

begin_interface
DECL|interface|AS2Constants
specifier|public
interface|interface
name|AS2Constants
block|{
comment|/**      * The Value of User Agent Header used by AS2 Camel Component.      */
DECL|field|HTTP_USER_AGENT
specifier|public
specifier|static
specifier|final
name|String
name|HTTP_USER_AGENT
init|=
literal|"Camel AS2 Component"
decl_stmt|;
comment|/**      * The Value of Origin Server Header used by AS2 Camel Component.      */
DECL|field|HTTP_ORIGIN_SERVER
specifier|public
specifier|static
specifier|final
name|String
name|HTTP_ORIGIN_SERVER
init|=
literal|"Camel AS2 Component"
decl_stmt|;
comment|/**      * Fully Qualified Domain Name used by AS2 Camel Component in Message ID Header.      */
DECL|field|HTTP_MESSAGE_ID_FQDN
specifier|public
specifier|static
specifier|final
name|String
name|HTTP_MESSAGE_ID_FQDN
init|=
literal|"camel.apache.org"
decl_stmt|;
comment|/**      * The Value of User Agent Header used by AS2 Camel Component.      */
DECL|field|MIME_VERSION
specifier|public
specifier|static
specifier|final
name|String
name|MIME_VERSION
init|=
literal|"1.0"
decl_stmt|;
comment|//
comment|// HTTP Context Attribute Names
comment|//
comment|/**      * HTTP Context Attribute Name for HTTP Client Connection object stored in context.      */
DECL|field|HTTP_CLIENT_CONNECTION
specifier|public
specifier|static
specifier|final
name|String
name|HTTP_CLIENT_CONNECTION
init|=
name|HttpCoreContext
operator|.
name|HTTP_CONNECTION
decl_stmt|;
comment|/**      * HTTP Context Attribute Name for HTTP Client Processor object stored in context.      */
DECL|field|HTTP_CLIENT_PROCESSOR
specifier|public
specifier|static
specifier|final
name|String
name|HTTP_CLIENT_PROCESSOR
init|=
literal|"http.processor"
decl_stmt|;
comment|/**      * HTTP Context Attribute Name for HTTP Client Fully Qualified Domain Name (FQDN) stored in context.      */
DECL|field|HTTP_CLIENT_FQDN
specifier|public
specifier|static
specifier|final
name|String
name|HTTP_CLIENT_FQDN
init|=
literal|"client.fqdn"
decl_stmt|;
comment|/**      * HTTP Context Attribute Name for HTTP Server Connection object stored in context.      */
DECL|field|HTTP_SERVER_CONNECTION
specifier|public
specifier|static
specifier|final
name|String
name|HTTP_SERVER_CONNECTION
init|=
literal|"http.server.connection"
decl_stmt|;
comment|/**      * HTTP Context Attribute Name for HTTP Server Processor object stored in context.      */
DECL|field|HTTP_SERVER_PROCESSOR
specifier|public
specifier|static
specifier|final
name|String
name|HTTP_SERVER_PROCESSOR
init|=
literal|"http.server.processor"
decl_stmt|;
comment|/**      * HTTP Context Attribute Name for HTTP Server Service object stored in context.      */
DECL|field|HTTP_SERVER_SERVICE
specifier|public
specifier|static
specifier|final
name|String
name|HTTP_SERVER_SERVICE
init|=
literal|"http.server.service"
decl_stmt|;
comment|//
comment|// AS2 MIME Content Types
comment|//
comment|/**      * Application EDIFACT content type      */
DECL|field|APPLICATION_EDIFACT_MIME_TYPE
specifier|public
specifier|static
specifier|final
name|String
name|APPLICATION_EDIFACT_MIME_TYPE
init|=
literal|"Application/EDIFACT"
decl_stmt|;
block|}
end_interface

end_unit

