begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.jmx
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|jmx
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
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|Endpoint
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
name|annotations
operator|.
name|Component
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
name|DefaultComponent
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
name|PropertyBindingSupport
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
name|PropertiesHelper
import|;
end_import

begin_comment
comment|/**  * Component for connecting JMX Notification events to a camel route.  * The endpoint created from this component allows users to specify  * an ObjectName to listen to and any JMX Notifications received from  * that object will flow into the route.  */
end_comment

begin_class
annotation|@
name|Component
argument_list|(
literal|"jmx"
argument_list|)
DECL|class|JMXComponent
specifier|public
class|class
name|JMXComponent
extends|extends
name|DefaultComponent
block|{
DECL|method|JMXComponent ()
specifier|public
name|JMXComponent
parameter_list|()
block|{     }
annotation|@
name|Override
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|createEndpoint (String uri, String remaining, Map<String, Object> parameters)
specifier|protected
name|Endpoint
name|createEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|String
name|remaining
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|parameters
parameter_list|)
throws|throws
name|Exception
block|{
name|JMXEndpoint
name|endpoint
init|=
operator|new
name|JMXEndpoint
argument_list|(
name|uri
argument_list|,
name|this
argument_list|)
decl_stmt|;
name|PropertyBindingSupport
operator|.
name|bindProperties
argument_list|(
name|getCamelContext
argument_list|()
argument_list|,
name|endpoint
argument_list|,
name|parameters
argument_list|)
expr_stmt|;
name|endpoint
operator|.
name|setServerURL
argument_list|(
name|remaining
argument_list|)
expr_stmt|;
name|Map
name|objectProperties
init|=
name|PropertiesHelper
operator|.
name|extractProperties
argument_list|(
name|parameters
argument_list|,
literal|"key."
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|objectProperties
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|endpoint
operator|.
name|setObjectProperties
argument_list|(
name|objectProperties
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|endpoint
operator|.
name|getObjectDomain
argument_list|()
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Must specify domain"
argument_list|)
throw|;
block|}
if|if
condition|(
name|endpoint
operator|.
name|getObjectName
argument_list|()
operator|==
literal|null
operator|&&
name|endpoint
operator|.
name|getObjectProperties
argument_list|()
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Must specify object name or object properties"
argument_list|)
throw|;
block|}
return|return
name|endpoint
return|;
block|}
block|}
end_class

end_unit

