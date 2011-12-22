begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.language.groovy
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|language
operator|.
name|groovy
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStreamReader
import|;
end_import

begin_import
import|import
name|groovy
operator|.
name|lang
operator|.
name|GroovyShell
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
name|builder
operator|.
name|RouteBuilder
import|;
end_import

begin_comment
comment|/**  * @version   */
end_comment

begin_class
DECL|class|GroovyRouteBuilder
specifier|public
specifier|abstract
class|class
name|GroovyRouteBuilder
extends|extends
name|RouteBuilder
block|{
DECL|method|GroovyRouteBuilder ()
specifier|public
name|GroovyRouteBuilder
parameter_list|()
block|{
name|init
argument_list|()
expr_stmt|;
block|}
DECL|method|GroovyRouteBuilder (CamelContext context)
specifier|public
name|GroovyRouteBuilder
parameter_list|(
name|CamelContext
name|context
parameter_list|)
block|{
name|super
argument_list|(
name|context
argument_list|)
expr_stmt|;
name|init
argument_list|()
expr_stmt|;
block|}
DECL|method|init ()
specifier|private
name|void
name|init
parameter_list|()
block|{
name|ClassLoader
name|loader
init|=
name|getClass
argument_list|()
operator|.
name|getClassLoader
argument_list|()
decl_stmt|;
name|GroovyShell
name|shell
init|=
operator|new
name|GroovyShell
argument_list|(
name|loader
argument_list|)
decl_stmt|;
name|shell
operator|.
name|evaluate
argument_list|(
operator|new
name|InputStreamReader
argument_list|(
name|loader
operator|.
name|getResourceAsStream
argument_list|(
literal|"org/apache/camel/language/groovy/ConfigureCamel.groovy"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
comment|// TODO compile Groovy as part of build!
comment|//new ConfigureCamel().run();
block|}
block|}
end_class

end_unit

