begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.telegram.service
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|telegram
operator|.
name|service
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|Consumes
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|GET
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|POST
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|Path
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|PathParam
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|Produces
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|QueryParam
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|core
operator|.
name|MediaType
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|telegram
operator|.
name|model
operator|.
name|EditMessageLiveLocationMessage
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|telegram
operator|.
name|model
operator|.
name|MessageResult
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|telegram
operator|.
name|model
operator|.
name|OutgoingTextMessage
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|telegram
operator|.
name|model
operator|.
name|SendLocationMessage
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|telegram
operator|.
name|model
operator|.
name|UpdateResult
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|jaxrs
operator|.
name|ext
operator|.
name|multipart
operator|.
name|Attachment
import|;
end_import

begin_comment
comment|/**  * Describes the Telegram Bot APIs.  */
end_comment

begin_interface
annotation|@
name|Path
argument_list|(
literal|"/"
argument_list|)
DECL|interface|RestBotAPI
specifier|public
interface|interface
name|RestBotAPI
block|{
DECL|field|BOT_API_DEFAULT_URL
name|String
name|BOT_API_DEFAULT_URL
init|=
literal|"https://api.telegram.org"
decl_stmt|;
annotation|@
name|GET
annotation|@
name|Path
argument_list|(
literal|"/bot{authorizationToken}/getUpdates"
argument_list|)
annotation|@
name|Produces
argument_list|(
name|MediaType
operator|.
name|APPLICATION_JSON
argument_list|)
DECL|method|getUpdates ( @athParamR) String authorizationToken, @QueryParam(R) Long offset, @QueryParam(R) Integer limit, @QueryParam(R) Integer timeoutSeconds)
name|UpdateResult
name|getUpdates
parameter_list|(
annotation|@
name|PathParam
argument_list|(
literal|"authorizationToken"
argument_list|)
name|String
name|authorizationToken
parameter_list|,
annotation|@
name|QueryParam
argument_list|(
literal|"offset"
argument_list|)
name|Long
name|offset
parameter_list|,
annotation|@
name|QueryParam
argument_list|(
literal|"limit"
argument_list|)
name|Integer
name|limit
parameter_list|,
annotation|@
name|QueryParam
argument_list|(
literal|"timeout"
argument_list|)
name|Integer
name|timeoutSeconds
parameter_list|)
function_decl|;
annotation|@
name|POST
annotation|@
name|Path
argument_list|(
literal|"/bot{authorizationToken}/sendMessage"
argument_list|)
annotation|@
name|Consumes
argument_list|(
name|MediaType
operator|.
name|APPLICATION_JSON
argument_list|)
annotation|@
name|Produces
argument_list|(
name|MediaType
operator|.
name|APPLICATION_JSON
argument_list|)
DECL|method|sendMessage ( @athParamR) String authorizationToken, OutgoingTextMessage message)
name|MessageResult
name|sendMessage
parameter_list|(
annotation|@
name|PathParam
argument_list|(
literal|"authorizationToken"
argument_list|)
name|String
name|authorizationToken
parameter_list|,
name|OutgoingTextMessage
name|message
parameter_list|)
function_decl|;
annotation|@
name|POST
annotation|@
name|Path
argument_list|(
literal|"/bot{authorizationToken}/sendPhoto"
argument_list|)
annotation|@
name|Consumes
argument_list|(
name|MediaType
operator|.
name|MULTIPART_FORM_DATA
argument_list|)
annotation|@
name|Produces
argument_list|(
name|MediaType
operator|.
name|APPLICATION_JSON
argument_list|)
DECL|method|sendPhoto (@athParamR) String authorizationToken, List<Attachment> attachments)
name|MessageResult
name|sendPhoto
parameter_list|(
annotation|@
name|PathParam
argument_list|(
literal|"authorizationToken"
argument_list|)
name|String
name|authorizationToken
parameter_list|,
name|List
argument_list|<
name|Attachment
argument_list|>
name|attachments
parameter_list|)
function_decl|;
annotation|@
name|POST
annotation|@
name|Path
argument_list|(
literal|"/bot{authorizationToken}/sendAudio"
argument_list|)
annotation|@
name|Consumes
argument_list|(
name|MediaType
operator|.
name|MULTIPART_FORM_DATA
argument_list|)
annotation|@
name|Produces
argument_list|(
name|MediaType
operator|.
name|APPLICATION_JSON
argument_list|)
DECL|method|sendAudio (@athParamR) String authorizationToken, List<Attachment> attachments)
name|MessageResult
name|sendAudio
parameter_list|(
annotation|@
name|PathParam
argument_list|(
literal|"authorizationToken"
argument_list|)
name|String
name|authorizationToken
parameter_list|,
name|List
argument_list|<
name|Attachment
argument_list|>
name|attachments
parameter_list|)
function_decl|;
annotation|@
name|POST
annotation|@
name|Path
argument_list|(
literal|"/bot{authorizationToken}/sendVideo"
argument_list|)
annotation|@
name|Consumes
argument_list|(
name|MediaType
operator|.
name|MULTIPART_FORM_DATA
argument_list|)
annotation|@
name|Produces
argument_list|(
name|MediaType
operator|.
name|APPLICATION_JSON
argument_list|)
DECL|method|sendVideo (@athParamR) String authorizationToken, List<Attachment> attachments)
name|MessageResult
name|sendVideo
parameter_list|(
annotation|@
name|PathParam
argument_list|(
literal|"authorizationToken"
argument_list|)
name|String
name|authorizationToken
parameter_list|,
name|List
argument_list|<
name|Attachment
argument_list|>
name|attachments
parameter_list|)
function_decl|;
annotation|@
name|POST
annotation|@
name|Path
argument_list|(
literal|"/bot{authorizationToken}/sendDocument"
argument_list|)
annotation|@
name|Consumes
argument_list|(
name|MediaType
operator|.
name|MULTIPART_FORM_DATA
argument_list|)
annotation|@
name|Produces
argument_list|(
name|MediaType
operator|.
name|APPLICATION_JSON
argument_list|)
DECL|method|sendDocument (@athParamR) String authorizationToken, List<Attachment> attachments)
name|MessageResult
name|sendDocument
parameter_list|(
annotation|@
name|PathParam
argument_list|(
literal|"authorizationToken"
argument_list|)
name|String
name|authorizationToken
parameter_list|,
name|List
argument_list|<
name|Attachment
argument_list|>
name|attachments
parameter_list|)
function_decl|;
annotation|@
name|POST
annotation|@
name|Path
argument_list|(
literal|"/bot{authorizationToken}/sendLocation"
argument_list|)
annotation|@
name|Consumes
argument_list|(
name|MediaType
operator|.
name|APPLICATION_JSON
argument_list|)
annotation|@
name|Produces
argument_list|(
name|MediaType
operator|.
name|APPLICATION_JSON
argument_list|)
DECL|method|sendLocation (@athParamR) String authorizationToken, SendLocationMessage location)
name|MessageResult
name|sendLocation
parameter_list|(
annotation|@
name|PathParam
argument_list|(
literal|"authorizationToken"
argument_list|)
name|String
name|authorizationToken
parameter_list|,
name|SendLocationMessage
name|location
parameter_list|)
function_decl|;
annotation|@
name|POST
annotation|@
name|Path
argument_list|(
literal|"/bot{authorizationToken}/editMessageLiveLocation"
argument_list|)
annotation|@
name|Consumes
argument_list|(
name|MediaType
operator|.
name|APPLICATION_JSON
argument_list|)
annotation|@
name|Produces
argument_list|(
name|MediaType
operator|.
name|APPLICATION_JSON
argument_list|)
DECL|method|editMessageLiveLocation (@athParamR) String authorizationToken, EditMessageLiveLocationMessage location)
name|MessageResult
name|editMessageLiveLocation
parameter_list|(
annotation|@
name|PathParam
argument_list|(
literal|"authorizationToken"
argument_list|)
name|String
name|authorizationToken
parameter_list|,
name|EditMessageLiveLocationMessage
name|location
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

