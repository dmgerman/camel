begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.ignite.compute
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|ignite
operator|.
name|compute
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
name|util
operator|.
name|Map
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
name|Consumer
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
name|Processor
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
name|Producer
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
name|component
operator|.
name|ignite
operator|.
name|AbstractIgniteEndpoint
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
name|component
operator|.
name|ignite
operator|.
name|ClusterGroupExpression
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
name|component
operator|.
name|ignite
operator|.
name|IgniteComponent
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
name|spi
operator|.
name|UriEndpoint
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
name|spi
operator|.
name|UriParam
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|ignite
operator|.
name|Ignite
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|ignite
operator|.
name|IgniteCompute
import|;
end_import

begin_comment
comment|/**  * Ignite Compute endpoint.  */
end_comment

begin_class
annotation|@
name|UriEndpoint
argument_list|(
name|scheme
operator|=
literal|"ignite:compute"
argument_list|,
name|title
operator|=
literal|"Ignite Compute"
argument_list|,
name|syntax
operator|=
literal|"ignite:compute:endpointId"
argument_list|,
name|label
operator|=
literal|"nosql,cache,compute"
argument_list|,
name|producerOnly
operator|=
literal|true
argument_list|)
DECL|class|IgniteComputeEndpoint
specifier|public
class|class
name|IgniteComputeEndpoint
extends|extends
name|AbstractIgniteEndpoint
block|{
annotation|@
name|UriParam
DECL|field|clusterGroupExpression
specifier|private
name|ClusterGroupExpression
name|clusterGroupExpression
decl_stmt|;
annotation|@
name|UriParam
DECL|field|executionType
specifier|private
name|IgniteComputeExecutionType
name|executionType
decl_stmt|;
annotation|@
name|UriParam
DECL|field|taskName
specifier|private
name|String
name|taskName
decl_stmt|;
annotation|@
name|UriParam
DECL|field|computeName
specifier|private
name|String
name|computeName
decl_stmt|;
annotation|@
name|UriParam
DECL|field|timeoutMillis
specifier|private
name|Long
name|timeoutMillis
decl_stmt|;
DECL|method|IgniteComputeEndpoint (String uri, URI remainingUri, Map<String, Object> parameters, IgniteComponent igniteComponent)
specifier|public
name|IgniteComputeEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|URI
name|remainingUri
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|parameters
parameter_list|,
name|IgniteComponent
name|igniteComponent
parameter_list|)
throws|throws
name|ClassNotFoundException
block|{
name|super
argument_list|(
name|uri
argument_list|,
name|igniteComponent
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createProducer ()
specifier|public
name|Producer
name|createProducer
parameter_list|()
throws|throws
name|Exception
block|{
return|return
operator|new
name|IgniteComputeProducer
argument_list|(
name|this
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|createConsumer (Processor processor)
specifier|public
name|Consumer
name|createConsumer
parameter_list|(
name|Processor
name|processor
parameter_list|)
throws|throws
name|Exception
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"The Ignite Compute endpoint does not support consumers."
argument_list|)
throw|;
block|}
DECL|method|createIgniteCompute ()
specifier|public
name|IgniteCompute
name|createIgniteCompute
parameter_list|()
block|{
name|Ignite
name|ignite
init|=
name|ignite
argument_list|()
decl_stmt|;
name|IgniteCompute
name|compute
init|=
name|clusterGroupExpression
operator|==
literal|null
condition|?
name|ignite
operator|.
name|compute
argument_list|()
else|:
name|ignite
operator|.
name|compute
argument_list|(
name|clusterGroupExpression
operator|.
name|getClusterGroup
argument_list|(
name|ignite
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|computeName
operator|!=
literal|null
condition|)
block|{
name|compute
operator|=
name|compute
operator|.
name|withName
argument_list|(
name|computeName
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|timeoutMillis
operator|!=
literal|null
condition|)
block|{
name|compute
operator|=
name|compute
operator|.
name|withTimeout
argument_list|(
name|timeoutMillis
argument_list|)
expr_stmt|;
block|}
return|return
name|compute
return|;
block|}
comment|/**      * Gets the execution type of this producer.      *       * @return      */
DECL|method|getExecutionType ()
specifier|public
name|IgniteComputeExecutionType
name|getExecutionType
parameter_list|()
block|{
return|return
name|executionType
return|;
block|}
comment|/**      * Sets the execution type of this producer.      *       * @param executionType      */
DECL|method|setExecutionType (IgniteComputeExecutionType executionType)
specifier|public
name|void
name|setExecutionType
parameter_list|(
name|IgniteComputeExecutionType
name|executionType
parameter_list|)
block|{
name|this
operator|.
name|executionType
operator|=
name|executionType
expr_stmt|;
block|}
comment|/**      * Gets the task name, only applicable if using the {@link IgniteComputeExecutionType#EXECUTE} execution type.      *       * @return      */
DECL|method|getTaskName ()
specifier|public
name|String
name|getTaskName
parameter_list|()
block|{
return|return
name|taskName
return|;
block|}
comment|/**      * Sets the task name, only applicable if using the {@link IgniteComputeExecutionType#EXECUTE} execution type.      *       * @param taskName      */
DECL|method|setTaskName (String taskName)
specifier|public
name|void
name|setTaskName
parameter_list|(
name|String
name|taskName
parameter_list|)
block|{
name|this
operator|.
name|taskName
operator|=
name|taskName
expr_stmt|;
block|}
comment|/**      * Gets the name of the compute job, which will be set via {@link IgniteCompute#withName(String)}.       *       * @return      */
DECL|method|getComputeName ()
specifier|public
name|String
name|getComputeName
parameter_list|()
block|{
return|return
name|computeName
return|;
block|}
comment|/**      * Sets the name of the compute job, which will be set via {@link IgniteCompute#withName(String)}.      *       * @param computeName      */
DECL|method|setComputeName (String computeName)
specifier|public
name|void
name|setComputeName
parameter_list|(
name|String
name|computeName
parameter_list|)
block|{
name|this
operator|.
name|computeName
operator|=
name|computeName
expr_stmt|;
block|}
comment|/**      * Gets the timeout interval for triggered jobs, in milliseconds, which will be set via {@link IgniteCompute#withTimeout(long)}.      *       * @return      */
DECL|method|getTimeoutMillis ()
specifier|public
name|Long
name|getTimeoutMillis
parameter_list|()
block|{
return|return
name|timeoutMillis
return|;
block|}
comment|/**      * Sets the timeout interval for triggered jobs, in milliseconds, which will be set via {@link IgniteCompute#withTimeout(long)}.      *       * @param timeoutMillis      */
DECL|method|setTimeoutMillis (Long timeoutMillis)
specifier|public
name|void
name|setTimeoutMillis
parameter_list|(
name|Long
name|timeoutMillis
parameter_list|)
block|{
name|this
operator|.
name|timeoutMillis
operator|=
name|timeoutMillis
expr_stmt|;
block|}
block|}
end_class

end_unit

