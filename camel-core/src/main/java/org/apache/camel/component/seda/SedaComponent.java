begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.seda
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|seda
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|BlockingQueue
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|LinkedBlockingQueue
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
name|Exchange
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
name|DefaultComponent
import|;
end_import

begin_comment
comment|/**  * An implementation of the<a href="http://camel.apache.org/seda.html">SEDA components</a>  * for asynchronous SEDA exchanges on a {@link BlockingQueue} within a CamelContext  *  * @version $Revision$  */
end_comment

begin_class
DECL|class|SedaComponent
specifier|public
class|class
name|SedaComponent
extends|extends
name|DefaultComponent
block|{
DECL|method|createQueue (String uri, Map parameters)
specifier|public
name|BlockingQueue
argument_list|<
name|Exchange
argument_list|>
name|createQueue
parameter_list|(
name|String
name|uri
parameter_list|,
name|Map
name|parameters
parameter_list|)
block|{
name|int
name|size
init|=
name|getAndRemoveParameter
argument_list|(
name|parameters
argument_list|,
literal|"size"
argument_list|,
name|Integer
operator|.
name|class
argument_list|,
literal|1000
argument_list|)
decl_stmt|;
return|return
operator|new
name|LinkedBlockingQueue
argument_list|<
name|Exchange
argument_list|>
argument_list|(
name|size
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|createEndpoint (String uri, String remaining, Map parameters)
specifier|protected
name|Endpoint
name|createEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|String
name|remaining
parameter_list|,
name|Map
name|parameters
parameter_list|)
throws|throws
name|Exception
block|{
return|return
operator|new
name|SedaEndpoint
argument_list|(
name|uri
argument_list|,
name|this
argument_list|,
name|parameters
argument_list|)
return|;
block|}
block|}
end_class

end_unit

