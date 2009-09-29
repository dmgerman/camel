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
name|spi
operator|.
name|ManagementStrategy
import|;
end_import

begin_import
import|import
name|org
operator|.
name|fusesource
operator|.
name|commons
operator|.
name|management
operator|.
name|Statistic
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|jmx
operator|.
name|export
operator|.
name|annotation
operator|.
name|ManagedAttribute
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|jmx
operator|.
name|export
operator|.
name|annotation
operator|.
name|ManagedOperation
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|jmx
operator|.
name|export
operator|.
name|annotation
operator|.
name|ManagedResource
import|;
end_import

begin_class
annotation|@
name|ManagedResource
argument_list|(
name|description
operator|=
literal|"Managed Counter"
argument_list|)
DECL|class|ManagedCounter
specifier|public
class|class
name|ManagedCounter
block|{
DECL|field|exchangesTotal
specifier|protected
name|Statistic
name|exchangesTotal
decl_stmt|;
DECL|method|init (ManagementStrategy strategy)
specifier|public
name|void
name|init
parameter_list|(
name|ManagementStrategy
name|strategy
parameter_list|)
block|{
name|this
operator|.
name|exchangesTotal
operator|=
name|strategy
operator|.
name|createStatistic
argument_list|(
literal|"org.apache.camel.exchangesTotal"
argument_list|,
name|this
argument_list|,
name|Statistic
operator|.
name|UpdateMode
operator|.
name|COUNTER
argument_list|)
expr_stmt|;
block|}
annotation|@
name|ManagedOperation
argument_list|(
name|description
operator|=
literal|"Reset counters"
argument_list|)
DECL|method|reset ()
specifier|public
specifier|synchronized
name|void
name|reset
parameter_list|()
block|{
name|exchangesTotal
operator|.
name|reset
argument_list|()
expr_stmt|;
block|}
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Total number of exchanges"
argument_list|)
DECL|method|getExchangesTotal ()
specifier|public
name|long
name|getExchangesTotal
parameter_list|()
throws|throws
name|Exception
block|{
return|return
name|exchangesTotal
operator|.
name|getValue
argument_list|()
return|;
block|}
DECL|method|increment ()
specifier|public
specifier|synchronized
name|void
name|increment
parameter_list|()
block|{
name|exchangesTotal
operator|.
name|increment
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

