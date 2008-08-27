begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  *  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.jmxconnect.provider.camel
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|jmxconnect
operator|.
name|provider
operator|.
name|camel
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
name|jmxconnect
operator|.
name|CamelJmxConnectorServer
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|MBeanServer
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|remote
operator|.
name|JMXConnectorServer
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|remote
operator|.
name|JMXConnectorServerProvider
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|remote
operator|.
name|JMXServiceURL
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|MalformedURLException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_comment
comment|/**  * @version $Revision$  */
end_comment

begin_class
DECL|class|ServerProvider
specifier|public
class|class
name|ServerProvider
implements|implements
name|JMXConnectorServerProvider
block|{
DECL|method|newJMXConnectorServer (JMXServiceURL url, Map environment, MBeanServer server)
specifier|public
name|JMXConnectorServer
name|newJMXConnectorServer
parameter_list|(
name|JMXServiceURL
name|url
parameter_list|,
name|Map
name|environment
parameter_list|,
name|MBeanServer
name|server
parameter_list|)
throws|throws
name|IOException
block|{
return|return
operator|new
name|CamelJmxConnectorServer
argument_list|(
name|url
argument_list|,
name|environment
argument_list|,
name|server
argument_list|)
return|;
block|}
block|}
end_class

end_unit

