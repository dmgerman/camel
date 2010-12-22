begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.paxlogging
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|paxlogging
package|;
end_package

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|framework
operator|.
name|Bundle
import|;
end_import

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|framework
operator|.
name|ServiceFactory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|framework
operator|.
name|ServiceRegistration
import|;
end_import

begin_comment
comment|/**  * This service factory only purpose is to know which bundle is requesting the component  * so that the pax logging appender can be registered from the correct bundle context.  */
end_comment

begin_class
DECL|class|PaxLoggingServiceFactory
specifier|public
class|class
name|PaxLoggingServiceFactory
implements|implements
name|ServiceFactory
block|{
DECL|method|getService (Bundle bundle, ServiceRegistration serviceRegistration)
specifier|public
name|Object
name|getService
parameter_list|(
name|Bundle
name|bundle
parameter_list|,
name|ServiceRegistration
name|serviceRegistration
parameter_list|)
block|{
return|return
operator|new
name|PaxLoggingComponentResolver
argument_list|(
name|bundle
operator|.
name|getBundleContext
argument_list|()
argument_list|)
return|;
block|}
DECL|method|ungetService (Bundle bundle, ServiceRegistration serviceRegistration, Object o)
specifier|public
name|void
name|ungetService
parameter_list|(
name|Bundle
name|bundle
parameter_list|,
name|ServiceRegistration
name|serviceRegistration
parameter_list|,
name|Object
name|o
parameter_list|)
block|{     }
block|}
end_class

end_unit

