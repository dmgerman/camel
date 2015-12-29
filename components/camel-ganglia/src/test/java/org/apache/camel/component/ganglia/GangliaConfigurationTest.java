begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.ganglia
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|ganglia
package|;
end_package

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URI
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URISyntaxException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Before
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
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertEquals
import|;
end_import

begin_comment
comment|/**  * JUnit test class for<code>org.apache.camel.component.ganglia.GangliaConfiguration</code>  *   * @version   */
end_comment

begin_class
DECL|class|GangliaConfigurationTest
specifier|public
class|class
name|GangliaConfigurationTest
block|{
DECL|field|configuration
specifier|private
name|GangliaConfiguration
name|configuration
decl_stmt|;
annotation|@
name|Before
DECL|method|setUp ()
specifier|public
name|void
name|setUp
parameter_list|()
block|{
name|configuration
operator|=
operator|new
name|GangliaConfiguration
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|getterShouldReturnTheDefaultValues ()
specifier|public
name|void
name|getterShouldReturnTheDefaultValues
parameter_list|()
block|{
name|assertEquals
argument_list|(
literal|"239.2.11.71"
argument_list|,
name|configuration
operator|.
name|getHost
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|8649
argument_list|,
name|configuration
operator|.
name|getPort
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|true
argument_list|,
name|configuration
operator|.
name|getWireFormat31x
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|getterShouldReturnTheSetValues ()
specifier|public
name|void
name|getterShouldReturnTheSetValues
parameter_list|()
block|{
name|setNonDefaultValues
argument_list|(
name|configuration
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"10.10.1.1"
argument_list|,
name|configuration
operator|.
name|getHost
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|18649
argument_list|,
name|configuration
operator|.
name|getPort
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|false
argument_list|,
name|configuration
operator|.
name|getWireFormat31x
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|getterShouldReturnTheConfigureValuesFromURI ()
specifier|public
name|void
name|getterShouldReturnTheConfigureValuesFromURI
parameter_list|()
throws|throws
name|URISyntaxException
block|{
name|configuration
operator|.
name|configure
argument_list|(
operator|new
name|URI
argument_list|(
literal|"ganglia://192.168.1.1:28649?mode=UNICAST"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"192.168.1.1"
argument_list|,
name|configuration
operator|.
name|getHost
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|28649
argument_list|,
name|configuration
operator|.
name|getPort
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|cloneShouldReturnAnEqualInstance ()
specifier|public
name|void
name|cloneShouldReturnAnEqualInstance
parameter_list|()
block|{
name|setNonDefaultValues
argument_list|(
name|configuration
argument_list|)
expr_stmt|;
name|GangliaConfiguration
name|config
init|=
name|configuration
operator|.
name|copy
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
name|config
operator|.
name|getHost
argument_list|()
argument_list|,
name|configuration
operator|.
name|getHost
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|config
operator|.
name|getPort
argument_list|()
argument_list|,
name|configuration
operator|.
name|getPort
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|setNonDefaultValues (GangliaConfiguration config)
specifier|private
name|void
name|setNonDefaultValues
parameter_list|(
name|GangliaConfiguration
name|config
parameter_list|)
block|{
name|config
operator|.
name|setHost
argument_list|(
literal|"10.10.1.1"
argument_list|)
expr_stmt|;
name|config
operator|.
name|setPort
argument_list|(
literal|18649
argument_list|)
expr_stmt|;
name|config
operator|.
name|setWireFormat31x
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

