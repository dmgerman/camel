begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.impl
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|impl
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|CopyOnWriteArraySet
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
name|Service
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
name|StartupListener
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
name|ServiceHelper
import|;
end_import

begin_comment
comment|/**  * A {@link org.apache.camel.StartupListener} that defers starting {@link Service}s.  */
end_comment

begin_class
DECL|class|DeferServiceStartupListener
specifier|public
class|class
name|DeferServiceStartupListener
implements|implements
name|StartupListener
block|{
DECL|field|services
specifier|private
specifier|final
name|Set
argument_list|<
name|Service
argument_list|>
name|services
init|=
operator|new
name|CopyOnWriteArraySet
argument_list|<
name|Service
argument_list|>
argument_list|()
decl_stmt|;
DECL|method|addService (Service service)
specifier|public
name|void
name|addService
parameter_list|(
name|Service
name|service
parameter_list|)
block|{
name|services
operator|.
name|add
argument_list|(
name|service
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|onCamelContextStarted (CamelContext context, boolean alreadyStarted)
specifier|public
name|void
name|onCamelContextStarted
parameter_list|(
name|CamelContext
name|context
parameter_list|,
name|boolean
name|alreadyStarted
parameter_list|)
throws|throws
name|Exception
block|{
for|for
control|(
name|Service
name|service
range|:
name|services
control|)
block|{
name|ServiceHelper
operator|.
name|startService
argument_list|(
name|service
argument_list|)
expr_stmt|;
block|}
name|services
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

