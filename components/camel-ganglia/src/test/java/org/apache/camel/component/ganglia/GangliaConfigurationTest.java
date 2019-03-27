begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|info
operator|.
name|ganglia
operator|.
name|gmetric4j
operator|.
name|gmetric
operator|.
name|GMetric
import|;
end_import

begin_import
import|import
name|info
operator|.
name|ganglia
operator|.
name|gmetric4j
operator|.
name|gmetric
operator|.
name|GMetric
operator|.
name|UDPAddressingMode
import|;
end_import

begin_import
import|import
name|info
operator|.
name|ganglia
operator|.
name|gmetric4j
operator|.
name|gmetric
operator|.
name|GMetricSlope
import|;
end_import

begin_import
import|import
name|info
operator|.
name|ganglia
operator|.
name|gmetric4j
operator|.
name|gmetric
operator|.
name|GMetricType
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|jupiter
operator|.
name|api
operator|.
name|BeforeEach
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|jupiter
operator|.
name|api
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
name|jupiter
operator|.
name|api
operator|.
name|Assertions
operator|.
name|assertEquals
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|jupiter
operator|.
name|api
operator|.
name|Assertions
operator|.
name|assertNotNull
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|jupiter
operator|.
name|api
operator|.
name|Assertions
operator|.
name|assertTrue
import|;
end_import

begin_comment
comment|/**  * JUnit test class for  *<code>org.apache.camel.component.ganglia.GangliaConfiguration</code>  */
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
name|BeforeEach
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
DECL|method|getHostShouldReturnDefaultValue ()
specifier|public
name|void
name|getHostShouldReturnDefaultValue
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
block|}
annotation|@
name|Test
DECL|method|getPortShouldReturnDefaultValue ()
specifier|public
name|void
name|getPortShouldReturnDefaultValue
parameter_list|()
block|{
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
block|}
annotation|@
name|Test
DECL|method|getModeShouldReturnDefaultValue ()
specifier|public
name|void
name|getModeShouldReturnDefaultValue
parameter_list|()
block|{
name|assertEquals
argument_list|(
name|GMetric
operator|.
name|UDPAddressingMode
operator|.
name|MULTICAST
argument_list|,
name|configuration
operator|.
name|getMode
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|getTtlShouldReturnDefaultValue ()
specifier|public
name|void
name|getTtlShouldReturnDefaultValue
parameter_list|()
block|{
name|assertEquals
argument_list|(
literal|5
argument_list|,
name|configuration
operator|.
name|getTtl
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|getWireFormat31xShouldReturnDefaultValue ()
specifier|public
name|void
name|getWireFormat31xShouldReturnDefaultValue
parameter_list|()
block|{
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
DECL|method|getSpoofHostnameShouldReturnDefaultValue ()
specifier|public
name|void
name|getSpoofHostnameShouldReturnDefaultValue
parameter_list|()
block|{
name|assertEquals
argument_list|(
literal|null
argument_list|,
name|configuration
operator|.
name|getSpoofHostname
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|getGroupNameShouldReturnDefaultValue ()
specifier|public
name|void
name|getGroupNameShouldReturnDefaultValue
parameter_list|()
block|{
name|assertEquals
argument_list|(
literal|"Java"
argument_list|,
name|configuration
operator|.
name|getGroupName
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|getPrefixShouldReturnDefaultValue ()
specifier|public
name|void
name|getPrefixShouldReturnDefaultValue
parameter_list|()
block|{
name|assertEquals
argument_list|(
literal|null
argument_list|,
name|configuration
operator|.
name|getPrefix
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|getMetricNameShouldReturnDefaultValue ()
specifier|public
name|void
name|getMetricNameShouldReturnDefaultValue
parameter_list|()
block|{
name|assertEquals
argument_list|(
literal|"metric"
argument_list|,
name|configuration
operator|.
name|getMetricName
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|getTypeShouldReturnDefaultValue ()
specifier|public
name|void
name|getTypeShouldReturnDefaultValue
parameter_list|()
block|{
name|assertEquals
argument_list|(
name|GMetricType
operator|.
name|STRING
argument_list|,
name|configuration
operator|.
name|getType
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|getSlopeShouldReturnDefaultValue ()
specifier|public
name|void
name|getSlopeShouldReturnDefaultValue
parameter_list|()
block|{
name|assertEquals
argument_list|(
name|GMetricSlope
operator|.
name|BOTH
argument_list|,
name|configuration
operator|.
name|getSlope
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|getUnitsShouldReturnDefaultValue ()
specifier|public
name|void
name|getUnitsShouldReturnDefaultValue
parameter_list|()
block|{
name|assertEquals
argument_list|(
literal|""
argument_list|,
name|configuration
operator|.
name|getUnits
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|isWireFormat31xShouldReturnDefaultValue ()
specifier|public
name|void
name|isWireFormat31xShouldReturnDefaultValue
parameter_list|()
block|{
name|assertTrue
argument_list|(
name|configuration
operator|.
name|isWireFormat31x
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|getTMaxShouldReturnDefaultValue ()
specifier|public
name|void
name|getTMaxShouldReturnDefaultValue
parameter_list|()
block|{
name|assertEquals
argument_list|(
literal|60
argument_list|,
name|configuration
operator|.
name|getTmax
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|getDMaxShouldReturnDefaultValue ()
specifier|public
name|void
name|getDMaxShouldReturnDefaultValue
parameter_list|()
block|{
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|configuration
operator|.
name|getDmax
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|toStringShouldSucceed ()
specifier|public
name|void
name|toStringShouldSucceed
parameter_list|()
block|{
name|assertNotNull
argument_list|(
name|configuration
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|configureShouldSetHostAndPort ()
specifier|public
name|void
name|configureShouldSetHostAndPort
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
literal|"ganglia://192.168.1.1:28649"
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
DECL|method|configureWithoutHostShouldKeepDefaultHost ()
specifier|public
name|void
name|configureWithoutHostShouldKeepDefaultHost
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
literal|"ganglia://:28649"
argument_list|)
argument_list|)
expr_stmt|;
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
block|}
annotation|@
name|Test
DECL|method|configureWithoutPortShouldKeepDefaultPort ()
specifier|public
name|void
name|configureWithoutPortShouldKeepDefaultPort
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
literal|"ganglia://192.168.1.1:"
argument_list|)
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
block|}
annotation|@
name|Test
DECL|method|cloneShouldSucceed ()
specifier|public
name|void
name|cloneShouldSucceed
parameter_list|()
block|{
name|GangliaConfiguration
name|clone
init|=
name|configuration
operator|.
name|copy
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"239.2.11.71"
argument_list|,
name|clone
operator|.
name|getHost
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|8649
argument_list|,
name|clone
operator|.
name|getPort
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|UDPAddressingMode
operator|.
name|MULTICAST
argument_list|,
name|clone
operator|.
name|getMode
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|5
argument_list|,
name|clone
operator|.
name|getTtl
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|true
argument_list|,
name|clone
operator|.
name|getWireFormat31x
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|null
argument_list|,
name|clone
operator|.
name|getSpoofHostname
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Java"
argument_list|,
name|clone
operator|.
name|getGroupName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|null
argument_list|,
name|clone
operator|.
name|getPrefix
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"metric"
argument_list|,
name|clone
operator|.
name|getMetricName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|GMetricType
operator|.
name|STRING
argument_list|,
name|clone
operator|.
name|getType
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|GMetricSlope
operator|.
name|BOTH
argument_list|,
name|clone
operator|.
name|getSlope
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|""
argument_list|,
name|clone
operator|.
name|getUnits
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|clone
operator|.
name|isWireFormat31x
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|60
argument_list|,
name|clone
operator|.
name|getTmax
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|clone
operator|.
name|getDmax
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

