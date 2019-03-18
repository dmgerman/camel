begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.impl.event
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|impl
operator|.
name|event
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
name|spi
operator|.
name|CamelEvent
import|;
end_import

begin_class
DECL|class|RouteRemovedEvent
specifier|public
class|class
name|RouteRemovedEvent
extends|extends
name|AbstractRouteEvent
implements|implements
name|CamelEvent
operator|.
name|RouteRemovedEvent
block|{
DECL|field|serialVersionUID
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|7966471393751298719L
decl_stmt|;
DECL|method|RouteRemovedEvent (Route source)
specifier|public
name|RouteRemovedEvent
parameter_list|(
name|Route
name|source
parameter_list|)
block|{
name|super
argument_list|(
name|source
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"Removed route: "
operator|+
name|getRoute
argument_list|()
operator|.
name|getId
argument_list|()
return|;
block|}
block|}
end_class

end_unit

