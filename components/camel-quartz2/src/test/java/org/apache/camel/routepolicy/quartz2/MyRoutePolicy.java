begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.routepolicy.quartz2
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|routepolicy
operator|.
name|quartz2
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
name|Route
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
name|impl
operator|.
name|RoutePolicySupport
import|;
end_import

begin_comment
comment|/**  *  */
end_comment

begin_class
DECL|class|MyRoutePolicy
specifier|public
class|class
name|MyRoutePolicy
extends|extends
name|RoutePolicySupport
block|{
DECL|field|start
specifier|private
name|boolean
name|start
decl_stmt|;
DECL|field|stop
specifier|private
name|boolean
name|stop
decl_stmt|;
annotation|@
name|Override
DECL|method|onStart (Route route)
specifier|public
name|void
name|onStart
parameter_list|(
name|Route
name|route
parameter_list|)
block|{
name|start
operator|=
literal|true
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|onStop (Route route)
specifier|public
name|void
name|onStop
parameter_list|(
name|Route
name|route
parameter_list|)
block|{
name|stop
operator|=
literal|true
expr_stmt|;
block|}
DECL|method|isStart ()
specifier|public
name|boolean
name|isStart
parameter_list|()
block|{
return|return
name|start
return|;
block|}
DECL|method|isStop ()
specifier|public
name|boolean
name|isStop
parameter_list|()
block|{
return|return
name|stop
return|;
block|}
block|}
end_class

end_unit

