begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.couchdb
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|couchdb
package|;
end_package

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gson
operator|.
name|JsonElement
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gson
operator|.
name|JsonObject
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gson
operator|.
name|JsonParser
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gson
operator|.
name|JsonSyntaxException
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
name|Exchange
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
name|InvalidPayloadException
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
name|impl
operator|.
name|DefaultProducer
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
name|util
operator|.
name|ObjectHelper
import|;
end_import

begin_import
import|import
name|org
operator|.
name|lightcouch
operator|.
name|Response
import|;
end_import

begin_class
DECL|class|CouchDbProducer
specifier|public
class|class
name|CouchDbProducer
extends|extends
name|DefaultProducer
block|{
DECL|field|couchClient
specifier|private
specifier|final
name|CouchDbClientWrapper
name|couchClient
decl_stmt|;
DECL|method|CouchDbProducer (CouchDbEndpoint endpoint, CouchDbClientWrapper couchClient)
specifier|public
name|CouchDbProducer
parameter_list|(
name|CouchDbEndpoint
name|endpoint
parameter_list|,
name|CouchDbClientWrapper
name|couchClient
parameter_list|)
block|{
name|super
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
name|this
operator|.
name|couchClient
operator|=
name|couchClient
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|process (Exchange exchange)
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|JsonElement
name|json
init|=
name|getBodyAsJsonElement
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
name|String
name|operation
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|CouchDbConstants
operator|.
name|HEADER_METHOD
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|ObjectHelper
operator|.
name|isEmpty
argument_list|(
name|operation
argument_list|)
condition|)
block|{
name|Response
name|save
init|=
name|saveJsonElement
argument_list|(
name|json
argument_list|)
decl_stmt|;
if|if
condition|(
name|save
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|CouchDbException
argument_list|(
literal|"Could not save document [unknown reason]"
argument_list|,
name|exchange
argument_list|)
throw|;
block|}
if|if
condition|(
name|log
operator|.
name|isTraceEnabled
argument_list|()
condition|)
block|{
name|log
operator|.
name|trace
argument_list|(
literal|"Document saved [_id={}, _rev={}]"
argument_list|,
name|save
operator|.
name|getId
argument_list|()
argument_list|,
name|save
operator|.
name|getRev
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|CouchDbConstants
operator|.
name|HEADER_DOC_REV
argument_list|,
name|save
operator|.
name|getRev
argument_list|()
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|CouchDbConstants
operator|.
name|HEADER_DOC_ID
argument_list|,
name|save
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
if|if
condition|(
name|operation
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"DELETE"
argument_list|)
condition|)
block|{
name|Response
name|delete
init|=
name|deleteJsonElement
argument_list|(
name|json
argument_list|)
decl_stmt|;
if|if
condition|(
name|delete
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|CouchDbException
argument_list|(
literal|"Could not delete document [unknown reason]"
argument_list|,
name|exchange
argument_list|)
throw|;
block|}
if|if
condition|(
name|log
operator|.
name|isTraceEnabled
argument_list|()
condition|)
block|{
name|log
operator|.
name|trace
argument_list|(
literal|"Document saved [_id={}, _rev={}]"
argument_list|,
name|delete
operator|.
name|getId
argument_list|()
argument_list|,
name|delete
operator|.
name|getRev
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|CouchDbConstants
operator|.
name|HEADER_DOC_REV
argument_list|,
name|delete
operator|.
name|getRev
argument_list|()
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|CouchDbConstants
operator|.
name|HEADER_DOC_ID
argument_list|,
name|delete
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|getBodyAsJsonElement (Exchange exchange)
name|JsonElement
name|getBodyAsJsonElement
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|InvalidPayloadException
block|{
name|Object
name|body
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getMandatoryBody
argument_list|()
decl_stmt|;
if|if
condition|(
name|body
operator|instanceof
name|String
condition|)
block|{
try|try
block|{
return|return
operator|new
name|JsonParser
argument_list|()
operator|.
name|parse
argument_list|(
operator|(
name|String
operator|)
name|body
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|JsonSyntaxException
name|jse
parameter_list|)
block|{
throw|throw
operator|new
name|InvalidPayloadException
argument_list|(
name|exchange
argument_list|,
name|body
operator|.
name|getClass
argument_list|()
argument_list|)
throw|;
block|}
block|}
elseif|else
if|if
condition|(
name|body
operator|instanceof
name|JsonElement
condition|)
block|{
return|return
operator|(
name|JsonElement
operator|)
name|body
return|;
block|}
else|else
block|{
throw|throw
operator|new
name|InvalidPayloadException
argument_list|(
name|exchange
argument_list|,
name|body
operator|!=
literal|null
condition|?
name|body
operator|.
name|getClass
argument_list|()
else|:
literal|null
argument_list|)
throw|;
block|}
block|}
DECL|method|saveJsonElement (JsonElement json)
specifier|private
name|Response
name|saveJsonElement
parameter_list|(
name|JsonElement
name|json
parameter_list|)
block|{
name|Response
name|save
decl_stmt|;
if|if
condition|(
name|json
operator|instanceof
name|JsonObject
condition|)
block|{
name|JsonObject
name|obj
init|=
operator|(
name|JsonObject
operator|)
name|json
decl_stmt|;
if|if
condition|(
name|obj
operator|.
name|get
argument_list|(
literal|"_rev"
argument_list|)
operator|==
literal|null
condition|)
block|{
name|save
operator|=
name|couchClient
operator|.
name|save
argument_list|(
name|json
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|save
operator|=
name|couchClient
operator|.
name|update
argument_list|(
name|json
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|save
operator|=
name|couchClient
operator|.
name|save
argument_list|(
name|json
argument_list|)
expr_stmt|;
block|}
return|return
name|save
return|;
block|}
DECL|method|deleteJsonElement (JsonElement json)
specifier|private
name|Response
name|deleteJsonElement
parameter_list|(
name|JsonElement
name|json
parameter_list|)
block|{
name|Response
name|delete
decl_stmt|;
if|if
condition|(
name|json
operator|instanceof
name|JsonObject
condition|)
block|{
name|JsonObject
name|obj
init|=
operator|(
name|JsonObject
operator|)
name|json
decl_stmt|;
name|delete
operator|=
name|couchClient
operator|.
name|remove
argument_list|(
name|obj
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|delete
operator|=
name|couchClient
operator|.
name|remove
argument_list|(
name|json
argument_list|)
expr_stmt|;
block|}
return|return
name|delete
return|;
block|}
block|}
end_class

end_unit

