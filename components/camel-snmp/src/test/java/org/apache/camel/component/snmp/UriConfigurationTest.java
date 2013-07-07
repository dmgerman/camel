begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.snmp
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|snmp
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
name|CamelContext
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
name|Endpoint
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
DECL|class|UriConfigurationTest
specifier|public
class|class
name|UriConfigurationTest
extends|extends
name|Assert
block|{
DECL|field|context
specifier|protected
name|CamelContext
name|context
init|=
operator|new
name|DefaultCamelContext
argument_list|()
decl_stmt|;
annotation|@
name|Test
DECL|method|testTrapReceiverConfiguration ()
specifier|public
name|void
name|testTrapReceiverConfiguration
parameter_list|()
throws|throws
name|Exception
block|{
name|Endpoint
name|endpoint
init|=
name|context
operator|.
name|getEndpoint
argument_list|(
literal|"snmp:0.0.0.0:1662?protocol=udp&type=TRAP&oids=1.3.6.1.2.1.7.5.1"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
literal|"Endpoint not an SnmpEndpoint: "
operator|+
name|endpoint
argument_list|,
name|endpoint
operator|instanceof
name|SnmpEndpoint
argument_list|)
expr_stmt|;
name|SnmpEndpoint
name|snmpEndpoint
init|=
operator|(
name|SnmpEndpoint
operator|)
name|endpoint
decl_stmt|;
name|assertEquals
argument_list|(
name|SnmpActionType
operator|.
name|TRAP
argument_list|,
name|snmpEndpoint
operator|.
name|getType
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"1.3.6.1.2.1.7.5.1"
argument_list|,
name|snmpEndpoint
operator|.
name|getOids
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"udp:0.0.0.0/1662"
argument_list|,
name|snmpEndpoint
operator|.
name|getAddress
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testTrapReceiverWithoutPortConfiguration ()
specifier|public
name|void
name|testTrapReceiverWithoutPortConfiguration
parameter_list|()
throws|throws
name|Exception
block|{
name|Endpoint
name|endpoint
init|=
name|context
operator|.
name|getEndpoint
argument_list|(
literal|"snmp:0.0.0.0?protocol=udp&type=TRAP&oids=1.3.6.1.2.1.7.5.1"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
literal|"Endpoint not an SnmpEndpoint: "
operator|+
name|endpoint
argument_list|,
name|endpoint
operator|instanceof
name|SnmpEndpoint
argument_list|)
expr_stmt|;
name|SnmpEndpoint
name|snmpEndpoint
init|=
operator|(
name|SnmpEndpoint
operator|)
name|endpoint
decl_stmt|;
name|assertEquals
argument_list|(
name|SnmpActionType
operator|.
name|TRAP
argument_list|,
name|snmpEndpoint
operator|.
name|getType
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"1.3.6.1.2.1.7.5.1"
argument_list|,
name|snmpEndpoint
operator|.
name|getOids
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"udp:0.0.0.0/162"
argument_list|,
name|snmpEndpoint
operator|.
name|getAddress
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testOidPollerConfiguration ()
specifier|public
name|void
name|testOidPollerConfiguration
parameter_list|()
throws|throws
name|Exception
block|{
name|Endpoint
name|endpoint
init|=
name|context
operator|.
name|getEndpoint
argument_list|(
literal|"snmp:127.0.0.1:1662?protocol=udp&type=POLL&oids=1.3.6.1.2.1.7.5.1"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
literal|"Endpoint not an SnmpEndpoint: "
operator|+
name|endpoint
argument_list|,
name|endpoint
operator|instanceof
name|SnmpEndpoint
argument_list|)
expr_stmt|;
name|SnmpEndpoint
name|snmpEndpoint
init|=
operator|(
name|SnmpEndpoint
operator|)
name|endpoint
decl_stmt|;
name|assertEquals
argument_list|(
name|SnmpActionType
operator|.
name|POLL
argument_list|,
name|snmpEndpoint
operator|.
name|getType
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"1.3.6.1.2.1.7.5.1"
argument_list|,
name|snmpEndpoint
operator|.
name|getOids
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"udp:127.0.0.1/1662"
argument_list|,
name|snmpEndpoint
operator|.
name|getAddress
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

