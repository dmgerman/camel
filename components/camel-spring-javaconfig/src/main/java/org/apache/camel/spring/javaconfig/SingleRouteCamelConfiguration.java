begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.spring.javaconfig
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spring
operator|.
name|javaconfig
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
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
name|builder
operator|.
name|RouteBuilder
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|context
operator|.
name|annotation
operator|.
name|Bean
import|;
end_import

begin_comment
comment|/**  * A useful base class for writing  *<a  * href="http://docs.spring.io/spring/docs/current/spring-framework-reference/html/beans.html#beans-annotation-config">  * Spring annotation-based</a> configurations to configure a {@link org.apache.camel.CamelContext} with a single  * {@link RouteBuilder} instance.  *<p/>  * You may want to use Spring's {@link org.springframework.stereotype.Component} annotation and mark your Camel  * {@link RouteBuilder} classes using this annotation and have it automatic discovered, if you are using  * Spring's annotation scanner.  */
end_comment

begin_class
DECL|class|SingleRouteCamelConfiguration
specifier|public
specifier|abstract
class|class
name|SingleRouteCamelConfiguration
extends|extends
name|CamelConfiguration
block|{
annotation|@
name|Override
annotation|@
name|Bean
DECL|method|routes ()
specifier|public
name|List
argument_list|<
name|RouteBuilder
argument_list|>
name|routes
parameter_list|()
block|{
return|return
name|Collections
operator|.
name|singletonList
argument_list|(
name|route
argument_list|()
argument_list|)
return|;
block|}
comment|/**      * Creates the single {@link RouteBuilder} to use in this configuration      */
DECL|method|route ()
specifier|public
specifier|abstract
name|RouteBuilder
name|route
parameter_list|()
function_decl|;
block|}
end_class

end_unit

