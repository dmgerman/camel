begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.ignite.idgen
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
name|idgen
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
name|spi
operator|.
name|Metadata
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
name|camel
operator|.
name|spi
operator|.
name|UriPath
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
name|util
operator|.
name|ObjectHelper
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
name|IgniteAtomicSequence
import|;
end_import

begin_comment
comment|/**  * The Ignite ID Generator endpoint is one of camel-ignite endpoints which allows you to interact with  *<a href="https://apacheignite.readme.io/docs/id-generator">Ignite Atomic Sequences and ID Generators</a>.  * This endpoint only supports producers.  */
end_comment

begin_class
annotation|@
name|UriEndpoint
argument_list|(
name|firstVersion
operator|=
literal|"2.17.0"
argument_list|,
name|scheme
operator|=
literal|"ignite-idgen"
argument_list|,
name|title
operator|=
literal|"Ignite ID Generator"
argument_list|,
name|syntax
operator|=
literal|"ignite-idgen:name"
argument_list|,
name|label
operator|=
literal|"nosql,cache,compute"
argument_list|,
name|producerOnly
operator|=
literal|true
argument_list|)
DECL|class|IgniteIdGenEndpoint
specifier|public
class|class
name|IgniteIdGenEndpoint
extends|extends
name|AbstractIgniteEndpoint
block|{
annotation|@
name|UriPath
annotation|@
name|Metadata
argument_list|(
name|required
operator|=
literal|true
argument_list|)
DECL|field|name
specifier|private
name|String
name|name
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"producer"
argument_list|)
DECL|field|batchSize
specifier|private
name|Integer
name|batchSize
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"producer"
argument_list|,
name|defaultValue
operator|=
literal|"0"
argument_list|)
DECL|field|initialValue
specifier|private
name|Long
name|initialValue
init|=
literal|0L
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"producer"
argument_list|)
DECL|field|operation
specifier|private
name|IgniteIdGenOperation
name|operation
decl_stmt|;
DECL|method|IgniteIdGenEndpoint (String endpointUri, String remaining, Map<String, Object> parameters, IgniteIdGenComponent igniteComponent)
specifier|public
name|IgniteIdGenEndpoint
parameter_list|(
name|String
name|endpointUri
parameter_list|,
name|String
name|remaining
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|parameters
parameter_list|,
name|IgniteIdGenComponent
name|igniteComponent
parameter_list|)
throws|throws
name|Exception
block|{
name|super
argument_list|(
name|endpointUri
argument_list|,
name|igniteComponent
argument_list|)
expr_stmt|;
name|name
operator|=
name|remaining
expr_stmt|;
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|name
argument_list|,
literal|"ID Generator name"
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
name|IgniteAtomicSequence
name|atomicSeq
init|=
name|ignite
argument_list|()
operator|.
name|atomicSequence
argument_list|(
name|name
argument_list|,
name|initialValue
argument_list|,
literal|false
argument_list|)
decl_stmt|;
if|if
condition|(
name|atomicSeq
operator|==
literal|null
condition|)
block|{
name|atomicSeq
operator|=
name|ignite
argument_list|()
operator|.
name|atomicSequence
argument_list|(
name|name
argument_list|,
name|initialValue
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|log
operator|.
name|info
argument_list|(
literal|"Created AtomicSequence of ID Generator with name {}."
argument_list|,
name|name
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|batchSize
operator|!=
literal|null
condition|)
block|{
name|atomicSeq
operator|.
name|batchSize
argument_list|(
name|batchSize
argument_list|)
expr_stmt|;
block|}
return|return
operator|new
name|IgniteIdGenProducer
argument_list|(
name|this
argument_list|,
name|atomicSeq
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
literal|"The Ignite Id Generator endpoint doesn't support consumers."
argument_list|)
throw|;
block|}
comment|/**      * Gets the name.      *       * @return name      */
DECL|method|getName ()
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
name|name
return|;
block|}
comment|/**      * The sequence name.      *       * @param name name      */
DECL|method|setName (String name)
specifier|public
name|void
name|setName
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|this
operator|.
name|name
operator|=
name|name
expr_stmt|;
block|}
comment|/**      * Gets the initial value.      *       * @return initial value      */
DECL|method|getInitialValue ()
specifier|public
name|Long
name|getInitialValue
parameter_list|()
block|{
return|return
name|initialValue
return|;
block|}
comment|/**      * The initial value.      *       * @param initialValue initial value      */
DECL|method|setInitialValue (Long initialValue)
specifier|public
name|void
name|setInitialValue
parameter_list|(
name|Long
name|initialValue
parameter_list|)
block|{
name|this
operator|.
name|initialValue
operator|=
name|initialValue
expr_stmt|;
block|}
comment|/**      * Gets the operation.      *       * @return operation      */
DECL|method|getOperation ()
specifier|public
name|IgniteIdGenOperation
name|getOperation
parameter_list|()
block|{
return|return
name|operation
return|;
block|}
comment|/**      * The operation to invoke on the Ignite ID Generator.      * Superseded by the IgniteConstants.IGNITE_IDGEN_OPERATION header in the IN message.      * Possible values: ADD_AND_GET, GET, GET_AND_ADD, GET_AND_INCREMENT, INCREMENT_AND_GET.      *       * @param operation operation      */
DECL|method|setOperation (IgniteIdGenOperation operation)
specifier|public
name|void
name|setOperation
parameter_list|(
name|IgniteIdGenOperation
name|operation
parameter_list|)
block|{
name|this
operator|.
name|operation
operator|=
name|operation
expr_stmt|;
block|}
comment|/**      * Gets the batch size.      *       * @return batch size      */
DECL|method|getBatchSize ()
specifier|public
name|Integer
name|getBatchSize
parameter_list|()
block|{
return|return
name|batchSize
return|;
block|}
comment|/**      * The batch size.      *       * @param batchSize batch size      */
DECL|method|setBatchSize (Integer batchSize)
specifier|public
name|void
name|setBatchSize
parameter_list|(
name|Integer
name|batchSize
parameter_list|)
block|{
name|this
operator|.
name|batchSize
operator|=
name|batchSize
expr_stmt|;
block|}
block|}
end_class

end_unit

