begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.schematron.processor
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|schematron
operator|.
name|processor
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|Templates
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Assert
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Test
import|;
end_import

begin_comment
comment|/**  * TemplateFactory Unit Test.  *  */
end_comment

begin_class
DECL|class|TemplatesFactoryTest
specifier|public
class|class
name|TemplatesFactoryTest
block|{
DECL|field|rules
specifier|private
name|String
name|rules
init|=
literal|"sch/schematron-1.sch"
decl_stmt|;
annotation|@
name|Test
DECL|method|testInstantiateAnInstanceOfTemplates ()
specifier|public
name|void
name|testInstantiateAnInstanceOfTemplates
parameter_list|()
throws|throws
name|Exception
block|{
name|TemplatesFactory
name|fac
init|=
name|TemplatesFactory
operator|.
name|newInstance
argument_list|()
decl_stmt|;
name|Templates
name|templates
init|=
name|fac
operator|.
name|newTemplates
argument_list|(
name|ClassLoader
operator|.
name|getSystemResourceAsStream
argument_list|(
name|rules
argument_list|)
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertNotNull
argument_list|(
name|templates
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

