begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.model.rest
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|model
operator|.
name|rest
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlAccessType
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlAccessorType
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlAttribute
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlRootElement
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlTransient
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
name|spi
operator|.
name|Metadata
import|;
end_import

begin_comment
comment|/**  * To specify the rest operation response messages using Swagger.  *<p/>  * This maps to the Swagger Response Message Object.  * see com.wordnik.swagger.model.ResponseMessage  * and https://github.com/swagger-api/swagger-spec/blob/master/versions/1.2.md#525-response-message-object.  */
end_comment

begin_class
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"rest"
argument_list|)
annotation|@
name|XmlRootElement
argument_list|(
name|name
operator|=
literal|"responseMessage"
argument_list|)
annotation|@
name|XmlAccessorType
argument_list|(
name|XmlAccessType
operator|.
name|FIELD
argument_list|)
DECL|class|RestOperationResponseMsgDefinition
specifier|public
class|class
name|RestOperationResponseMsgDefinition
block|{
annotation|@
name|XmlTransient
DECL|field|verb
specifier|private
name|VerbDefinition
name|verb
decl_stmt|;
annotation|@
name|XmlAttribute
argument_list|(
name|required
operator|=
literal|true
argument_list|)
DECL|field|code
specifier|private
name|int
name|code
decl_stmt|;
annotation|@
name|XmlAttribute
argument_list|(
name|required
operator|=
literal|true
argument_list|)
DECL|field|message
specifier|private
name|String
name|message
decl_stmt|;
annotation|@
name|XmlAttribute
annotation|@
name|Metadata
argument_list|(
name|defaultValue
operator|=
literal|""
argument_list|)
DECL|field|responseModel
specifier|private
name|String
name|responseModel
decl_stmt|;
DECL|method|RestOperationResponseMsgDefinition (VerbDefinition verb)
specifier|public
name|RestOperationResponseMsgDefinition
parameter_list|(
name|VerbDefinition
name|verb
parameter_list|)
block|{
name|this
operator|.
name|verb
operator|=
name|verb
expr_stmt|;
block|}
DECL|method|RestOperationResponseMsgDefinition ()
specifier|public
name|RestOperationResponseMsgDefinition
parameter_list|()
block|{     }
DECL|method|getCode ()
specifier|public
name|int
name|getCode
parameter_list|()
block|{
return|return
name|code
operator|!=
literal|0
condition|?
name|code
else|:
literal|200
return|;
block|}
comment|/**      * Sets the Swagger Operation's ResponseMessage code      */
DECL|method|setCode (int code)
specifier|public
name|void
name|setCode
parameter_list|(
name|int
name|code
parameter_list|)
block|{
name|this
operator|.
name|code
operator|=
name|code
expr_stmt|;
block|}
DECL|method|getResponseModel ()
specifier|public
name|String
name|getResponseModel
parameter_list|()
block|{
return|return
name|responseModel
operator|!=
literal|null
condition|?
name|responseModel
else|:
literal|""
return|;
block|}
comment|/**      * Sets the Swagger Operation's ResponseMessage responseModel      */
DECL|method|setResponseModel (String responseModel)
specifier|public
name|void
name|setResponseModel
parameter_list|(
name|String
name|responseModel
parameter_list|)
block|{
name|this
operator|.
name|responseModel
operator|=
name|responseModel
expr_stmt|;
block|}
DECL|method|getMessage ()
specifier|public
name|String
name|getMessage
parameter_list|()
block|{
return|return
name|message
operator|!=
literal|null
condition|?
name|message
else|:
literal|"success"
return|;
block|}
comment|/**      * Sets the Swagger Operation's ResponseMessage message      */
DECL|method|setMessage (String message)
specifier|public
name|void
name|setMessage
parameter_list|(
name|String
name|message
parameter_list|)
block|{
name|this
operator|.
name|message
operator|=
name|message
expr_stmt|;
block|}
comment|/**      * The return code      */
DECL|method|code (int code)
specifier|public
name|RestOperationResponseMsgDefinition
name|code
parameter_list|(
name|int
name|code
parameter_list|)
block|{
name|setCode
argument_list|(
name|code
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * The return message      */
DECL|method|message (String msg)
specifier|public
name|RestOperationResponseMsgDefinition
name|message
parameter_list|(
name|String
name|msg
parameter_list|)
block|{
name|setMessage
argument_list|(
name|msg
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * The response model      */
DECL|method|responseModel (Class<?> type)
specifier|public
name|RestOperationResponseMsgDefinition
name|responseModel
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|type
parameter_list|)
block|{
name|setResponseModel
argument_list|(
name|type
operator|.
name|getCanonicalName
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Ends the configuration of this response message      */
DECL|method|endResponseMessage ()
specifier|public
name|RestDefinition
name|endResponseMessage
parameter_list|()
block|{
name|verb
operator|.
name|getResponseMsgs
argument_list|()
operator|.
name|add
argument_list|(
name|this
argument_list|)
expr_stmt|;
return|return
name|verb
operator|.
name|getRest
argument_list|()
return|;
block|}
block|}
end_class

end_unit

