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
name|CamelJmxConnector
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
name|JMXConnector
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
name|JMXConnectorProvider
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
comment|/**  * @version $Revision: 1.1 $  */
end_comment

begin_class
DECL|class|ClientProvider
specifier|public
class|class
name|ClientProvider
implements|implements
name|JMXConnectorProvider
block|{
DECL|method|newJMXConnector (JMXServiceURL url, Map environment)
specifier|public
name|JMXConnector
name|newJMXConnector
parameter_list|(
name|JMXServiceURL
name|url
parameter_list|,
name|Map
name|environment
parameter_list|)
throws|throws
name|IOException
block|{
return|return
operator|new
name|CamelJmxConnector
argument_list|(
name|environment
argument_list|,
name|url
argument_list|)
return|;
block|}
block|}
end_class

end_unit

