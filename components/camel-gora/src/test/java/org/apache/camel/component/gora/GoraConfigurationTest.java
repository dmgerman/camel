begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.gora
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|gora
package|;
end_package

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
comment|/**  * GORA Configuration Tests  *  */
end_comment

begin_class
DECL|class|GoraConfigurationTest
specifier|public
class|class
name|GoraConfigurationTest
block|{
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|IllegalArgumentException
operator|.
name|class
argument_list|)
DECL|method|setKeyClassClassShouldThrowExceptionIfNull ()
specifier|public
name|void
name|setKeyClassClassShouldThrowExceptionIfNull
parameter_list|()
block|{
specifier|final
name|GoraConfiguration
name|conf
init|=
operator|new
name|GoraConfiguration
argument_list|()
decl_stmt|;
name|conf
operator|.
name|setValueClass
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|IllegalArgumentException
operator|.
name|class
argument_list|)
DECL|method|setKeyClassShouldThrowExceptionIfEmpty ()
specifier|public
name|void
name|setKeyClassShouldThrowExceptionIfEmpty
parameter_list|()
block|{
specifier|final
name|GoraConfiguration
name|conf
init|=
operator|new
name|GoraConfiguration
argument_list|()
decl_stmt|;
name|conf
operator|.
name|setValueClass
argument_list|(
literal|""
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|IllegalArgumentException
operator|.
name|class
argument_list|)
DECL|method|setValueClassClassShouldThrowExceptionIfNull ()
specifier|public
name|void
name|setValueClassClassShouldThrowExceptionIfNull
parameter_list|()
block|{
specifier|final
name|GoraConfiguration
name|conf
init|=
operator|new
name|GoraConfiguration
argument_list|()
decl_stmt|;
name|conf
operator|.
name|setValueClass
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|IllegalArgumentException
operator|.
name|class
argument_list|)
DECL|method|setValueClassClassShouldThrowExceptionIfEmpty ()
specifier|public
name|void
name|setValueClassClassShouldThrowExceptionIfEmpty
parameter_list|()
block|{
specifier|final
name|GoraConfiguration
name|conf
init|=
operator|new
name|GoraConfiguration
argument_list|()
decl_stmt|;
name|conf
operator|.
name|setValueClass
argument_list|(
literal|""
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|IllegalArgumentException
operator|.
name|class
argument_list|)
DECL|method|setDataStoreClassShouldThrowExceptionIfNull ()
specifier|public
name|void
name|setDataStoreClassShouldThrowExceptionIfNull
parameter_list|()
block|{
specifier|final
name|GoraConfiguration
name|conf
init|=
operator|new
name|GoraConfiguration
argument_list|()
decl_stmt|;
name|conf
operator|.
name|setDataStoreClass
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|IllegalArgumentException
operator|.
name|class
argument_list|)
DECL|method|setDataStoreClassShouldThrowExceptionIfEmpty ()
specifier|public
name|void
name|setDataStoreClassShouldThrowExceptionIfEmpty
parameter_list|()
block|{
specifier|final
name|GoraConfiguration
name|conf
init|=
operator|new
name|GoraConfiguration
argument_list|()
decl_stmt|;
name|conf
operator|.
name|setDataStoreClass
argument_list|(
literal|""
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|NullPointerException
operator|.
name|class
argument_list|)
DECL|method|setHadoopConfigurationShouldThrowExceptionIfNull ()
specifier|public
name|void
name|setHadoopConfigurationShouldThrowExceptionIfNull
parameter_list|()
block|{
specifier|final
name|GoraConfiguration
name|conf
init|=
operator|new
name|GoraConfiguration
argument_list|()
decl_stmt|;
name|conf
operator|.
name|setHadoopConfiguration
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

