begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|ManagedTypeConverterRegistryMBean
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
name|TypeConverterRegistry
import|;
end_import

begin_comment
comment|/**  *  */
end_comment

begin_class
annotation|@
name|ManagedResource
argument_list|(
name|description
operator|=
literal|"Managed TypeConverterRegistry"
argument_list|)
DECL|class|ManagedTypeConverterRegistry
specifier|public
class|class
name|ManagedTypeConverterRegistry
extends|extends
name|ManagedService
implements|implements
name|ManagedTypeConverterRegistryMBean
block|{
DECL|field|registry
specifier|private
specifier|final
name|TypeConverterRegistry
name|registry
decl_stmt|;
DECL|method|ManagedTypeConverterRegistry (CamelContext context, TypeConverterRegistry registry)
specifier|public
name|ManagedTypeConverterRegistry
parameter_list|(
name|CamelContext
name|context
parameter_list|,
name|TypeConverterRegistry
name|registry
parameter_list|)
block|{
name|super
argument_list|(
name|context
argument_list|,
name|registry
argument_list|)
expr_stmt|;
name|this
operator|.
name|registry
operator|=
name|registry
expr_stmt|;
name|this
operator|.
name|single
operator|=
literal|true
expr_stmt|;
block|}
DECL|method|getRegistry ()
specifier|public
name|TypeConverterRegistry
name|getRegistry
parameter_list|()
block|{
return|return
name|registry
return|;
block|}
DECL|method|getAttemptCounter ()
specifier|public
name|long
name|getAttemptCounter
parameter_list|()
block|{
return|return
name|registry
operator|.
name|getStatistics
argument_list|()
operator|.
name|getAttemptCounter
argument_list|()
return|;
block|}
DECL|method|getHitCounter ()
specifier|public
name|long
name|getHitCounter
parameter_list|()
block|{
return|return
name|registry
operator|.
name|getStatistics
argument_list|()
operator|.
name|getHitCounter
argument_list|()
return|;
block|}
DECL|method|getMissCounter ()
specifier|public
name|long
name|getMissCounter
parameter_list|()
block|{
return|return
name|registry
operator|.
name|getStatistics
argument_list|()
operator|.
name|getMissCounter
argument_list|()
return|;
block|}
DECL|method|getFailedCounter ()
specifier|public
name|long
name|getFailedCounter
parameter_list|()
block|{
return|return
name|registry
operator|.
name|getStatistics
argument_list|()
operator|.
name|getFailedCounter
argument_list|()
return|;
block|}
DECL|method|resetTypeConversionCounters ()
specifier|public
name|void
name|resetTypeConversionCounters
parameter_list|()
block|{
name|registry
operator|.
name|getStatistics
argument_list|()
operator|.
name|reset
argument_list|()
expr_stmt|;
block|}
DECL|method|isStatisticsEnabled ()
specifier|public
name|boolean
name|isStatisticsEnabled
parameter_list|()
block|{
return|return
name|registry
operator|.
name|getStatistics
argument_list|()
operator|.
name|isStatisticsEnabled
argument_list|()
return|;
block|}
DECL|method|setStatisticsEnabled (boolean statisticsEnabled)
specifier|public
name|void
name|setStatisticsEnabled
parameter_list|(
name|boolean
name|statisticsEnabled
parameter_list|)
block|{
name|registry
operator|.
name|getStatistics
argument_list|()
operator|.
name|setStatisticsEnabled
argument_list|(
name|statisticsEnabled
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

