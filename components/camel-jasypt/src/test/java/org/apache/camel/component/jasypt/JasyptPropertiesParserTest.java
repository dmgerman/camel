begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.jasypt
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|jasypt
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

begin_comment
comment|/**  * @version $Revision$  */
end_comment

begin_class
DECL|class|JasyptPropertiesParserTest
specifier|public
class|class
name|JasyptPropertiesParserTest
extends|extends
name|TestCase
block|{
DECL|method|testJasyptPropertiesParser ()
specifier|public
name|void
name|testJasyptPropertiesParser
parameter_list|()
throws|throws
name|Exception
block|{
name|JasyptPropertiesParser
name|parser
init|=
operator|new
name|JasyptPropertiesParser
argument_list|()
decl_stmt|;
name|parser
operator|.
name|setPassword
argument_list|(
literal|"secret"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"foo"
argument_list|,
name|parser
operator|.
name|parsePropertyValue
argument_list|(
literal|"foo"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"tiger"
argument_list|,
name|parser
operator|.
name|parsePropertyValue
argument_list|(
literal|"ENC(bsW9uV37gQ0QHFu7KO03Ww==)"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

