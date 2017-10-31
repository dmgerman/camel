begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.yql.configuration
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|yql
operator|.
name|configuration
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
name|component
operator|.
name|yql
operator|.
name|exception
operator|.
name|YqlException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Rule
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

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|rules
operator|.
name|ExpectedException
import|;
end_import

begin_class
DECL|class|YqlConfigurationValidatorTest
specifier|public
class|class
name|YqlConfigurationValidatorTest
block|{
annotation|@
name|Rule
DECL|field|thrown
specifier|public
specifier|final
name|ExpectedException
name|thrown
init|=
name|ExpectedException
operator|.
name|none
argument_list|()
decl_stmt|;
annotation|@
name|Test
DECL|method|testGoodQuery ()
specifier|public
name|void
name|testGoodQuery
parameter_list|()
block|{
comment|// given
specifier|final
name|YqlConfiguration
name|yqlConfiguration
init|=
operator|new
name|YqlConfiguration
argument_list|()
decl_stmt|;
name|yqlConfiguration
operator|.
name|setQuery
argument_list|(
literal|"select * from ..."
argument_list|)
expr_stmt|;
name|yqlConfiguration
operator|.
name|setFormat
argument_list|(
literal|"json"
argument_list|)
expr_stmt|;
comment|// when
name|YqlConfigurationValidator
operator|.
name|validateProperties
argument_list|(
name|yqlConfiguration
argument_list|)
expr_stmt|;
comment|// then
comment|// no exception
block|}
annotation|@
name|Test
DECL|method|testMissingQuery ()
specifier|public
name|void
name|testMissingQuery
parameter_list|()
block|{
comment|// then
name|thrown
operator|.
name|expect
argument_list|(
name|YqlException
operator|.
name|class
argument_list|)
expr_stmt|;
name|thrown
operator|.
name|expectMessage
argument_list|(
literal|"<query> is not present or not valid!"
argument_list|)
expr_stmt|;
comment|// given
specifier|final
name|YqlConfiguration
name|yqlConfiguration
init|=
operator|new
name|YqlConfiguration
argument_list|()
decl_stmt|;
comment|// when
name|YqlConfigurationValidator
operator|.
name|validateProperties
argument_list|(
name|yqlConfiguration
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testJsonFormat ()
specifier|public
name|void
name|testJsonFormat
parameter_list|()
block|{
comment|// given
specifier|final
name|YqlConfiguration
name|yqlConfiguration
init|=
operator|new
name|YqlConfiguration
argument_list|()
decl_stmt|;
name|yqlConfiguration
operator|.
name|setQuery
argument_list|(
literal|"query"
argument_list|)
expr_stmt|;
name|yqlConfiguration
operator|.
name|setFormat
argument_list|(
literal|"json"
argument_list|)
expr_stmt|;
comment|// when
name|YqlConfigurationValidator
operator|.
name|validateProperties
argument_list|(
name|yqlConfiguration
argument_list|)
expr_stmt|;
comment|// then
comment|// no exception
block|}
annotation|@
name|Test
DECL|method|testXmlFormat ()
specifier|public
name|void
name|testXmlFormat
parameter_list|()
block|{
comment|// given
specifier|final
name|YqlConfiguration
name|yqlConfiguration
init|=
operator|new
name|YqlConfiguration
argument_list|()
decl_stmt|;
name|yqlConfiguration
operator|.
name|setQuery
argument_list|(
literal|"query"
argument_list|)
expr_stmt|;
name|yqlConfiguration
operator|.
name|setFormat
argument_list|(
literal|"xml"
argument_list|)
expr_stmt|;
comment|// when
name|YqlConfigurationValidator
operator|.
name|validateProperties
argument_list|(
name|yqlConfiguration
argument_list|)
expr_stmt|;
comment|// then
comment|// no exception
block|}
annotation|@
name|Test
DECL|method|testWrongFormat ()
specifier|public
name|void
name|testWrongFormat
parameter_list|()
block|{
comment|// then
name|thrown
operator|.
name|expect
argument_list|(
name|YqlException
operator|.
name|class
argument_list|)
expr_stmt|;
name|thrown
operator|.
name|expectMessage
argument_list|(
literal|"<format> is not valid!"
argument_list|)
expr_stmt|;
comment|// given
specifier|final
name|YqlConfiguration
name|yqlConfiguration
init|=
operator|new
name|YqlConfiguration
argument_list|()
decl_stmt|;
name|yqlConfiguration
operator|.
name|setQuery
argument_list|(
literal|"query"
argument_list|)
expr_stmt|;
name|yqlConfiguration
operator|.
name|setFormat
argument_list|(
literal|"format"
argument_list|)
expr_stmt|;
comment|// when
name|YqlConfigurationValidator
operator|.
name|validateProperties
argument_list|(
name|yqlConfiguration
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

