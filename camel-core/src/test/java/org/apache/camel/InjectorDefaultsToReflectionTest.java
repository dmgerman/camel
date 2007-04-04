begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  *  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel
package|package
name|org
operator|.
name|apache
operator|.
name|camel
package|;
end_package

begin_import
import|import
name|junit
operator|.
name|framework
operator|.
name|TestCase
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
name|DefaultCamelContext
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
name|ReflectionInjector
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
name|Injector
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|Log
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|LogFactory
import|;
end_import

begin_comment
comment|/**  * @version $Revision$  */
end_comment

begin_class
DECL|class|InjectorDefaultsToReflectionTest
specifier|public
class|class
name|InjectorDefaultsToReflectionTest
extends|extends
name|TestCase
block|{
DECL|field|log
specifier|private
specifier|static
specifier|final
specifier|transient
name|Log
name|log
init|=
name|LogFactory
operator|.
name|getLog
argument_list|(
name|InjectorDefaultsToReflectionTest
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|method|testInjectorIsReflectionByDefault ()
specifier|public
name|void
name|testInjectorIsReflectionByDefault
parameter_list|()
throws|throws
name|Exception
block|{
name|Injector
name|injector
init|=
operator|new
name|DefaultCamelContext
argument_list|()
operator|.
name|getInjector
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
literal|"Injector should be reflection based but was: "
operator|+
name|injector
argument_list|,
name|injector
operator|instanceof
name|ReflectionInjector
argument_list|)
expr_stmt|;
name|log
operator|.
name|debug
argument_list|(
literal|"Found injector: "
operator|+
name|injector
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

