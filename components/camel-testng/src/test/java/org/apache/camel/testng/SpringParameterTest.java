begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  *  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.testng
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|testng
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Properties
import|;
end_import

begin_import
import|import
name|org
operator|.
name|testng
operator|.
name|annotations
operator|.
name|DataProvider
import|;
end_import

begin_import
import|import
name|org
operator|.
name|testng
operator|.
name|annotations
operator|.
name|Test
import|;
end_import

begin_comment
comment|/**  * @version $Revision$  */
end_comment

begin_class
DECL|class|SpringParameterTest
specifier|public
class|class
name|SpringParameterTest
extends|extends
name|SpringRunner
block|{
annotation|@
name|DataProvider
argument_list|(
name|name
operator|=
literal|"appContextAndProperties"
argument_list|)
DECL|method|createData ()
specifier|public
name|Object
index|[]
index|[]
name|createData
parameter_list|()
block|{
return|return
operator|new
name|Object
index|[]
index|[]
block|{
block|{
literal|"spring.xml"
block|,
name|createProperties
argument_list|(
literal|"foo"
argument_list|,
literal|"param1"
argument_list|)
block|}
block|,
block|{
literal|"spring.xml"
block|,
name|createProperties
argument_list|(
literal|"foo"
argument_list|,
literal|"param2"
argument_list|)
block|}
block|}
return|;
block|}
annotation|@
name|Test
argument_list|(
name|dataProvider
operator|=
literal|"appContextAndProperties"
argument_list|)
DECL|method|assertApplicationContextStarts (String applicationContextLocations, Properties properties)
specifier|public
name|void
name|assertApplicationContextStarts
parameter_list|(
name|String
name|applicationContextLocations
parameter_list|,
name|Properties
name|properties
parameter_list|)
throws|throws
name|Exception
block|{
name|super
operator|.
name|assertApplicationContextStarts
argument_list|(
name|applicationContextLocations
argument_list|,
name|properties
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

