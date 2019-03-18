begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.influxdb
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|influxdb
package|;
end_package

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|UnknownHostException
import|;
end_import

begin_import
import|import static
name|junit
operator|.
name|framework
operator|.
name|TestCase
operator|.
name|assertNotNull
import|;
end_import

begin_import
import|import
name|org
operator|.
name|influxdb
operator|.
name|InfluxDB
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
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
name|annotation
operator|.
name|Bean
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
name|annotation
operator|.
name|Configuration
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|mockito
operator|.
name|Mockito
operator|.
name|*
import|;
end_import

begin_class
annotation|@
name|Configuration
DECL|class|MockedInfluxDbConfiguration
specifier|public
class|class
name|MockedInfluxDbConfiguration
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|MockedInfluxDbConfiguration
operator|.
name|class
argument_list|)
decl_stmt|;
annotation|@
name|Bean
DECL|method|influxDbBean ()
specifier|public
name|InfluxDB
name|influxDbBean
parameter_list|()
throws|throws
name|UnknownHostException
block|{
if|if
condition|(
name|LOG
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Creating new instance of a mocked influx db connection"
argument_list|)
expr_stmt|;
block|}
name|InfluxDB
name|mockedDbConnection
init|=
name|mock
argument_list|(
name|InfluxDB
operator|.
name|class
argument_list|)
decl_stmt|;
comment|//InfluxDB mockedDbConnection = InfluxDBFactory.connect("http://127.0.0.1:8086", "root", "root");
name|assertNotNull
argument_list|(
name|mockedDbConnection
argument_list|)
expr_stmt|;
return|return
name|mockedDbConnection
return|;
block|}
block|}
end_class

end_unit

