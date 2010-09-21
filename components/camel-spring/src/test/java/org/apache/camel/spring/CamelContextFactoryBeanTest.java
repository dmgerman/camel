begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.spring
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spring
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
name|JavaUuidGenerator
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
name|SimpleUuidGenerator
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
name|UuidGenerator
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
name|support
operator|.
name|StaticApplicationContext
import|;
end_import

begin_comment
comment|/**  * @version $Revision: $  */
end_comment

begin_class
DECL|class|CamelContextFactoryBeanTest
specifier|public
class|class
name|CamelContextFactoryBeanTest
extends|extends
name|TestCase
block|{
DECL|field|factory
specifier|private
name|CamelContextFactoryBean
name|factory
decl_stmt|;
DECL|method|setUp ()
specifier|protected
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|factory
operator|=
operator|new
name|CamelContextFactoryBean
argument_list|()
expr_stmt|;
name|factory
operator|.
name|setId
argument_list|(
literal|"camelContext"
argument_list|)
expr_stmt|;
block|}
DECL|method|testGetDefaultUuidGenerator ()
specifier|public
name|void
name|testGetDefaultUuidGenerator
parameter_list|()
throws|throws
name|Exception
block|{
name|factory
operator|.
name|setApplicationContext
argument_list|(
operator|new
name|StaticApplicationContext
argument_list|()
argument_list|)
expr_stmt|;
name|factory
operator|.
name|afterPropertiesSet
argument_list|()
expr_stmt|;
name|UuidGenerator
name|uuidGenerator
init|=
name|factory
operator|.
name|getContext
argument_list|()
operator|.
name|getUuidGenerator
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|uuidGenerator
operator|instanceof
name|JavaUuidGenerator
argument_list|)
expr_stmt|;
block|}
DECL|method|testGetCustomUuidGenerator ()
specifier|public
name|void
name|testGetCustomUuidGenerator
parameter_list|()
throws|throws
name|Exception
block|{
name|StaticApplicationContext
name|applicationContext
init|=
operator|new
name|StaticApplicationContext
argument_list|()
decl_stmt|;
name|applicationContext
operator|.
name|registerSingleton
argument_list|(
literal|"uuidGenerator"
argument_list|,
name|SimpleUuidGenerator
operator|.
name|class
argument_list|)
expr_stmt|;
name|factory
operator|.
name|setApplicationContext
argument_list|(
name|applicationContext
argument_list|)
expr_stmt|;
name|factory
operator|.
name|afterPropertiesSet
argument_list|()
expr_stmt|;
name|UuidGenerator
name|uuidGenerator
init|=
name|factory
operator|.
name|getContext
argument_list|()
operator|.
name|getUuidGenerator
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|uuidGenerator
operator|instanceof
name|SimpleUuidGenerator
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

