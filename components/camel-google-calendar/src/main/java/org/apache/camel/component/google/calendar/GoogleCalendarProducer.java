begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.google.calendar
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
name|calendar
operator|.
name|internal
operator|.
name|GoogleCalendarApiName
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
name|calendar
operator|.
name|internal
operator|.
name|GoogleCalendarPropertiesHelper
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
name|IntrospectionSupport
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
name|util
operator|.
name|component
operator|.
name|ApiMethod
import|;
end_import

begin_comment
comment|/**  * The GoogleCalendar producer.  */
end_comment

begin_class
DECL|class|GoogleCalendarProducer
specifier|public
class|class
name|GoogleCalendarProducer
extends|extends
name|AbstractApiProducer
argument_list|<
name|GoogleCalendarApiName
argument_list|,
name|GoogleCalendarConfiguration
argument_list|>
block|{
DECL|method|GoogleCalendarProducer (GoogleCalendarEndpoint endpoint)
specifier|public
name|GoogleCalendarProducer
parameter_list|(
name|GoogleCalendarEndpoint
name|endpoint
parameter_list|)
block|{
name|super
argument_list|(
name|endpoint
argument_list|,
name|GoogleCalendarPropertiesHelper
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
name|setProperty
argument_list|(
name|properties
argument_list|,
name|request
argument_list|,
literal|"q"
argument_list|)
expr_stmt|;
name|setProperty
argument_list|(
name|properties
argument_list|,
name|request
argument_list|,
literal|"maxResults"
argument_list|)
expr_stmt|;
name|setProperty
argument_list|(
name|properties
argument_list|,
name|request
argument_list|,
literal|"pageToken"
argument_list|)
expr_stmt|;
name|setProperty
argument_list|(
name|properties
argument_list|,
name|request
argument_list|,
literal|"fields"
argument_list|)
expr_stmt|;
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
DECL|method|setProperty (Map<String, Object> properties, AbstractGoogleClientRequest request, String key)
specifier|private
name|void
name|setProperty
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|properties
parameter_list|,
name|AbstractGoogleClientRequest
name|request
parameter_list|,
name|String
name|key
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|properties
operator|.
name|containsKey
argument_list|(
name|key
argument_list|)
condition|)
block|{
name|IntrospectionSupport
operator|.
name|setProperty
argument_list|(
name|getEndpoint
argument_list|()
operator|.
name|getCamelContext
argument_list|()
operator|.
name|getTypeConverter
argument_list|()
argument_list|,
name|request
argument_list|,
name|key
argument_list|,
name|properties
operator|.
name|get
argument_list|(
name|key
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

