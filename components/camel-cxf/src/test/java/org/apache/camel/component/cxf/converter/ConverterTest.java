begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.cxf.converter
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|cxf
operator|.
name|converter
package|;
end_package

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

begin_class
DECL|class|ConverterTest
specifier|public
class|class
name|ConverterTest
extends|extends
name|Assert
block|{
annotation|@
name|Test
DECL|method|testToClassesList ()
specifier|public
name|void
name|testToClassesList
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|classString
init|=
literal|"java.lang.String, "
operator|+
literal|"org.apache.camel.component.cxf.converter.ConverterTest ;"
operator|+
literal|"java.lang.StringBuffer"
decl_stmt|;
name|List
argument_list|<
name|Class
argument_list|>
name|classList
init|=
name|CxfConverter
operator|.
name|toClassList
argument_list|(
name|classString
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Get the wrong number of classes"
argument_list|,
name|classList
operator|.
name|size
argument_list|()
argument_list|,
literal|3
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Get the wrong the class"
argument_list|,
name|classList
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|,
name|String
operator|.
name|class
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Get the wrong the class"
argument_list|,
name|classList
operator|.
name|get
argument_list|(
literal|1
argument_list|)
argument_list|,
name|ConverterTest
operator|.
name|class
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Get the wrong the class"
argument_list|,
name|classList
operator|.
name|get
argument_list|(
literal|2
argument_list|)
argument_list|,
name|StringBuffer
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

