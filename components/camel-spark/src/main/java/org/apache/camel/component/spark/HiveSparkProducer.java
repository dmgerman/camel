begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.spark
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|spark
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
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
name|support
operator|.
name|DefaultProducer
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|spark
operator|.
name|sql
operator|.
name|Dataset
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|spark
operator|.
name|sql
operator|.
name|Row
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|spark
operator|.
name|sql
operator|.
name|hive
operator|.
name|HiveContext
import|;
end_import

begin_class
DECL|class|HiveSparkProducer
specifier|public
class|class
name|HiveSparkProducer
extends|extends
name|DefaultProducer
block|{
DECL|method|HiveSparkProducer (SparkEndpoint endpoint)
specifier|public
name|HiveSparkProducer
parameter_list|(
name|SparkEndpoint
name|endpoint
parameter_list|)
block|{
name|super
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|process (Exchange exchange)
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|HiveContext
name|hiveContext
init|=
name|resolveHiveContext
argument_list|()
decl_stmt|;
name|String
name|sql
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|Dataset
argument_list|<
name|Row
argument_list|>
name|resultFrame
init|=
name|hiveContext
operator|.
name|sql
argument_list|(
name|sql
argument_list|)
decl_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
name|getEndpoint
argument_list|()
operator|.
name|isCollect
argument_list|()
condition|?
name|resultFrame
operator|.
name|collectAsList
argument_list|()
else|:
name|resultFrame
operator|.
name|count
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getEndpoint ()
specifier|public
name|SparkEndpoint
name|getEndpoint
parameter_list|()
block|{
return|return
operator|(
name|SparkEndpoint
operator|)
name|super
operator|.
name|getEndpoint
argument_list|()
return|;
block|}
comment|// Helpers
DECL|method|resolveHiveContext ()
specifier|protected
name|HiveContext
name|resolveHiveContext
parameter_list|()
block|{
name|Set
argument_list|<
name|HiveContext
argument_list|>
name|hiveContexts
init|=
name|getEndpoint
argument_list|()
operator|.
name|getComponent
argument_list|()
operator|.
name|getCamelContext
argument_list|()
operator|.
name|getRegistry
argument_list|()
operator|.
name|findByType
argument_list|(
name|HiveContext
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|hiveContexts
operator|.
name|size
argument_list|()
operator|==
literal|1
condition|)
block|{
return|return
name|hiveContexts
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
return|;
block|}
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

