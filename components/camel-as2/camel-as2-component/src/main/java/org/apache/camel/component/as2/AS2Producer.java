begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.as2
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
package|;
end_package

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
name|component
operator|.
name|as2
operator|.
name|api
operator|.
name|entity
operator|.
name|DispositionNotificationMultipartReportEntity
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
name|as2
operator|.
name|internal
operator|.
name|AS2ApiName
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
name|as2
operator|.
name|internal
operator|.
name|AS2Constants
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
name|as2
operator|.
name|internal
operator|.
name|AS2PropertiesHelper
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
name|support
operator|.
name|component
operator|.
name|AbstractApiProducer
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|HttpEntity
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|HttpResponse
import|;
end_import

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
comment|/**  * The AS2 producer.  */
end_comment

begin_class
DECL|class|AS2Producer
specifier|public
class|class
name|AS2Producer
extends|extends
name|AbstractApiProducer
argument_list|<
name|AS2ApiName
argument_list|,
name|AS2Configuration
argument_list|>
block|{
DECL|method|AS2Producer (AS2Endpoint endpoint)
specifier|public
name|AS2Producer
parameter_list|(
name|AS2Endpoint
name|endpoint
parameter_list|)
block|{
name|super
argument_list|(
name|endpoint
argument_list|,
name|AS2PropertiesHelper
operator|.
name|getHelper
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|interceptResult (Object methodResult, Exchange resultExchange)
specifier|public
name|void
name|interceptResult
parameter_list|(
name|Object
name|methodResult
parameter_list|,
name|Exchange
name|resultExchange
parameter_list|)
block|{
name|HttpCoreContext
name|context
init|=
operator|(
name|HttpCoreContext
operator|)
name|methodResult
decl_stmt|;
name|resultExchange
operator|.
name|setProperty
argument_list|(
name|AS2Constants
operator|.
name|AS2_INTERCHANGE
argument_list|,
name|context
argument_list|)
expr_stmt|;
name|HttpResponse
name|response
init|=
name|context
operator|.
name|getResponse
argument_list|()
decl_stmt|;
name|HttpEntity
name|entity
init|=
name|response
operator|.
name|getEntity
argument_list|()
decl_stmt|;
if|if
condition|(
name|entity
operator|!=
literal|null
operator|&&
name|entity
operator|instanceof
name|DispositionNotificationMultipartReportEntity
condition|)
block|{
name|resultExchange
operator|.
name|getOut
argument_list|()
operator|.
name|setBody
argument_list|(
name|entity
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|resultExchange
operator|.
name|getOut
argument_list|()
operator|.
name|setBody
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

