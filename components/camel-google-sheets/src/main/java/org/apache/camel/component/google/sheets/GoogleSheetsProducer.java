begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.google.sheets
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
name|sheets
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
operator|.
name|Entry
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|api
operator|.
name|client
operator|.
name|googleapis
operator|.
name|services
operator|.
name|AbstractGoogleClientRequest
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
name|ExtendedCamelContext
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
name|RuntimeCamelException
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
name|google
operator|.
name|sheets
operator|.
name|internal
operator|.
name|GoogleSheetsApiName
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
name|google
operator|.
name|sheets
operator|.
name|internal
operator|.
name|GoogleSheetsPropertiesHelper
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
name|BeanIntrospection
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
name|camel
operator|.
name|support
operator|.
name|component
operator|.
name|ApiMethod
import|;
end_import

begin_comment
comment|/**  * The GoogleSheets producer.  */
end_comment

begin_class
DECL|class|GoogleSheetsProducer
specifier|public
class|class
name|GoogleSheetsProducer
extends|extends
name|AbstractApiProducer
argument_list|<
name|GoogleSheetsApiName
argument_list|,
name|GoogleSheetsConfiguration
argument_list|>
block|{
DECL|method|GoogleSheetsProducer (GoogleSheetsEndpoint endpoint)
specifier|public
name|GoogleSheetsProducer
parameter_list|(
name|GoogleSheetsEndpoint
name|endpoint
parameter_list|)
block|{
name|super
argument_list|(
name|endpoint
argument_list|,
name|GoogleSheetsPropertiesHelper
operator|.
name|getHelper
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|doInvokeMethod (ApiMethod method, Map<String, Object> properties)
specifier|protected
name|Object
name|doInvokeMethod
parameter_list|(
name|ApiMethod
name|method
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|properties
parameter_list|)
throws|throws
name|RuntimeCamelException
block|{
name|AbstractGoogleClientRequest
argument_list|<
name|?
argument_list|>
name|request
init|=
operator|(
name|AbstractGoogleClientRequest
operator|)
name|super
operator|.
name|doInvokeMethod
argument_list|(
name|method
argument_list|,
name|properties
argument_list|)
decl_stmt|;
try|try
block|{
name|BeanIntrospection
name|beanIntrospection
init|=
name|getEndpoint
argument_list|()
operator|.
name|getCamelContext
argument_list|()
operator|.
name|adapt
argument_list|(
name|ExtendedCamelContext
operator|.
name|class
argument_list|)
operator|.
name|getBeanIntrospection
argument_list|()
decl_stmt|;
for|for
control|(
name|Entry
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|p
range|:
name|properties
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|beanIntrospection
operator|.
name|setProperty
argument_list|(
name|getEndpoint
argument_list|()
operator|.
name|getCamelContext
argument_list|()
argument_list|,
name|request
argument_list|,
name|p
operator|.
name|getKey
argument_list|()
argument_list|,
name|p
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|request
operator|.
name|execute
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeCamelException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

