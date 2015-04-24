begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.example.spring.boot
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|example
operator|.
name|spring
operator|.
name|boot
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
name|spring
operator|.
name|boot
operator|.
name|FatJarRouter
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
name|spring
operator|.
name|boot
operator|.
name|FatWarInitializer
import|;
end_import

begin_class
DECL|class|MySpringBootRouterWarInitializer
specifier|public
class|class
name|MySpringBootRouterWarInitializer
extends|extends
name|FatWarInitializer
block|{
annotation|@
name|Override
DECL|method|routerClass ()
specifier|protected
name|Class
argument_list|<
name|?
extends|extends
name|FatJarRouter
argument_list|>
name|routerClass
parameter_list|()
block|{
return|return
name|MySpringBootRouter
operator|.
name|class
return|;
block|}
block|}
end_class

end_unit

