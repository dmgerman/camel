begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.management
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|management
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
name|NonManagedService
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
name|TimerListenerManager
import|;
end_import

begin_comment
comment|/**  * The {@link org.apache.camel.support.TimerListenerManager} used for tracking load statistics.  *<p/>  * From Camel 2.13 onwards the {@link ManagedLoadTimer} is only enabled if  * {@link org.apache.camel.spi.ManagementAgent#getLoadStatisticsEnabled()} is enabled.  */
end_comment

begin_class
DECL|class|ManagedLoadTimer
specifier|public
class|class
name|ManagedLoadTimer
extends|extends
name|TimerListenerManager
implements|implements
name|NonManagedService
block|{
comment|// empty on purpose
block|}
end_class

end_unit

