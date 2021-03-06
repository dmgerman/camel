begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.management.mbean
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|management
operator|.
name|mbean
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
name|CamelContext
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
name|ServiceStatus
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
name|StatefulService
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
name|api
operator|.
name|management
operator|.
name|ManagedInstance
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
name|api
operator|.
name|management
operator|.
name|ManagedResource
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
name|api
operator|.
name|management
operator|.
name|mbean
operator|.
name|ManagedDataFormatMBean
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
name|DataFormat
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
name|DataFormatName
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
name|ManagementStrategy
import|;
end_import

begin_class
annotation|@
name|ManagedResource
argument_list|(
name|description
operator|=
literal|"Managed DataFormat"
argument_list|)
DECL|class|ManagedDataFormat
specifier|public
class|class
name|ManagedDataFormat
implements|implements
name|ManagedInstance
implements|,
name|ManagedDataFormatMBean
block|{
DECL|field|camelContext
specifier|private
specifier|final
name|CamelContext
name|camelContext
decl_stmt|;
DECL|field|dataFormat
specifier|private
specifier|final
name|DataFormat
name|dataFormat
decl_stmt|;
DECL|method|ManagedDataFormat (CamelContext camelContext, DataFormat dataFormat)
specifier|public
name|ManagedDataFormat
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|,
name|DataFormat
name|dataFormat
parameter_list|)
block|{
name|this
operator|.
name|camelContext
operator|=
name|camelContext
expr_stmt|;
name|this
operator|.
name|dataFormat
operator|=
name|dataFormat
expr_stmt|;
block|}
DECL|method|init (ManagementStrategy strategy)
specifier|public
name|void
name|init
parameter_list|(
name|ManagementStrategy
name|strategy
parameter_list|)
block|{
comment|// noop
block|}
DECL|method|getDataFormat ()
specifier|public
name|DataFormat
name|getDataFormat
parameter_list|()
block|{
return|return
name|dataFormat
return|;
block|}
DECL|method|getContext ()
specifier|public
name|CamelContext
name|getContext
parameter_list|()
block|{
return|return
name|camelContext
return|;
block|}
annotation|@
name|Override
DECL|method|getName ()
specifier|public
name|String
name|getName
parameter_list|()
block|{
if|if
condition|(
name|dataFormat
operator|instanceof
name|DataFormatName
condition|)
block|{
return|return
operator|(
operator|(
name|DataFormatName
operator|)
name|dataFormat
operator|)
operator|.
name|getDataFormatName
argument_list|()
return|;
block|}
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|getCamelId ()
specifier|public
name|String
name|getCamelId
parameter_list|()
block|{
return|return
name|camelContext
operator|.
name|getName
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|getCamelManagementName ()
specifier|public
name|String
name|getCamelManagementName
parameter_list|()
block|{
return|return
name|camelContext
operator|.
name|getManagementName
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|getState ()
specifier|public
name|String
name|getState
parameter_list|()
block|{
comment|// must use String type to be sure remote JMX can read the attribute without requiring Camel classes.
if|if
condition|(
name|dataFormat
operator|instanceof
name|StatefulService
condition|)
block|{
name|ServiceStatus
name|status
init|=
operator|(
operator|(
name|StatefulService
operator|)
name|dataFormat
operator|)
operator|.
name|getStatus
argument_list|()
decl_stmt|;
return|return
name|status
operator|.
name|name
argument_list|()
return|;
block|}
comment|// assume started if not a ServiceSupport instance
return|return
name|ServiceStatus
operator|.
name|Started
operator|.
name|name
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|getInstance ()
specifier|public
name|DataFormat
name|getInstance
parameter_list|()
block|{
return|return
name|dataFormat
return|;
block|}
block|}
end_class

end_unit

