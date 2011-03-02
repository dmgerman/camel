begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.jt400
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|jt400
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
name|java
operator|.
name|util
operator|.
name|Arrays
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
name|javax
operator|.
name|naming
operator|.
name|OperationNotSupportedException
import|;
end_import

begin_import
import|import
name|com
operator|.
name|ibm
operator|.
name|as400
operator|.
name|access
operator|.
name|AS400
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
name|CamelException
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
name|impl
operator|.
name|DefaultEndpoint
import|;
end_import

begin_class
DECL|class|Jt400PgmEndpoint
specifier|public
class|class
name|Jt400PgmEndpoint
extends|extends
name|DefaultEndpoint
block|{
DECL|field|programToExecute
specifier|private
name|String
name|programToExecute
decl_stmt|;
DECL|field|outputFieldsIdxArray
specifier|private
name|Integer
index|[]
name|outputFieldsIdxArray
decl_stmt|;
DECL|field|outputFieldsLengthArray
specifier|private
name|Integer
index|[]
name|outputFieldsLengthArray
decl_stmt|;
DECL|field|iSeries
specifier|private
name|AS400
name|iSeries
decl_stmt|;
comment|/**      * Creates a new AS/400 PGM CALL endpoint      */
DECL|method|Jt400PgmEndpoint (String endpointUri, Jt400Component component)
specifier|protected
name|Jt400PgmEndpoint
parameter_list|(
name|String
name|endpointUri
parameter_list|,
name|Jt400Component
name|component
parameter_list|)
throws|throws
name|CamelException
block|{
name|super
argument_list|(
name|endpointUri
argument_list|,
name|component
argument_list|)
expr_stmt|;
try|try
block|{
name|URI
name|uri
init|=
operator|new
name|URI
argument_list|(
name|endpointUri
argument_list|)
decl_stmt|;
name|String
index|[]
name|credentials
init|=
name|uri
operator|.
name|getUserInfo
argument_list|()
operator|.
name|split
argument_list|(
literal|":"
argument_list|)
decl_stmt|;
name|iSeries
operator|=
operator|new
name|AS400
argument_list|(
name|uri
operator|.
name|getHost
argument_list|()
argument_list|,
name|credentials
index|[
literal|0
index|]
argument_list|,
name|credentials
index|[
literal|1
index|]
argument_list|)
expr_stmt|;
name|programToExecute
operator|=
name|uri
operator|.
name|getPath
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|URISyntaxException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|CamelException
argument_list|(
literal|"Unable to parse URI for "
operator|+
name|endpointUri
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
DECL|method|Jt400PgmEndpoint (String endpointUri, String programToExecute, Map<String, Object> parameters, CamelContext camelContext)
specifier|public
name|Jt400PgmEndpoint
parameter_list|(
name|String
name|endpointUri
parameter_list|,
name|String
name|programToExecute
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|parameters
parameter_list|,
name|CamelContext
name|camelContext
parameter_list|)
block|{
name|super
argument_list|(
name|endpointUri
argument_list|,
name|camelContext
argument_list|)
expr_stmt|;
name|this
operator|.
name|programToExecute
operator|=
name|programToExecute
expr_stmt|;
block|}
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
name|Jt400PgmProducer
argument_list|(
name|this
argument_list|)
return|;
block|}
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
name|OperationNotSupportedException
argument_list|()
throw|;
block|}
DECL|method|isSingleton ()
specifier|public
name|boolean
name|isSingleton
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
annotation|@
name|Override
DECL|method|stop ()
specifier|public
name|void
name|stop
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|stop
argument_list|()
expr_stmt|;
if|if
condition|(
name|iSeries
operator|!=
literal|null
condition|)
block|{
name|iSeries
operator|.
name|disconnectAllServices
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|isFieldIdxForOuput (int idx)
specifier|public
name|boolean
name|isFieldIdxForOuput
parameter_list|(
name|int
name|idx
parameter_list|)
block|{
return|return
name|Arrays
operator|.
name|binarySearch
argument_list|(
name|outputFieldsIdxArray
argument_list|,
name|idx
argument_list|)
operator|>=
literal|0
return|;
block|}
DECL|method|getOutputFieldLength (int idx)
specifier|public
name|int
name|getOutputFieldLength
parameter_list|(
name|int
name|idx
parameter_list|)
block|{
return|return
name|outputFieldsLengthArray
index|[
name|idx
index|]
return|;
block|}
comment|// getters and setters
DECL|method|getProgramToExecute ()
specifier|public
name|String
name|getProgramToExecute
parameter_list|()
block|{
return|return
name|programToExecute
return|;
block|}
DECL|method|getiSeries ()
specifier|public
name|AS400
name|getiSeries
parameter_list|()
block|{
return|return
name|iSeries
return|;
block|}
DECL|method|setOutputFieldsIdx (String outputFieldsIdx)
specifier|public
name|void
name|setOutputFieldsIdx
parameter_list|(
name|String
name|outputFieldsIdx
parameter_list|)
block|{
if|if
condition|(
name|outputFieldsIdx
operator|!=
literal|null
condition|)
block|{
name|String
index|[]
name|outputArray
init|=
name|outputFieldsIdx
operator|.
name|split
argument_list|(
literal|","
argument_list|)
decl_stmt|;
name|outputFieldsIdxArray
operator|=
operator|new
name|Integer
index|[
name|outputArray
operator|.
name|length
index|]
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|outputArray
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|String
name|str
init|=
name|outputArray
index|[
name|i
index|]
decl_stmt|;
name|outputFieldsIdxArray
index|[
name|i
index|]
operator|=
name|Integer
operator|.
name|parseInt
argument_list|(
name|str
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|setFieldsLength (String fieldsLength)
specifier|public
name|void
name|setFieldsLength
parameter_list|(
name|String
name|fieldsLength
parameter_list|)
block|{
if|if
condition|(
name|fieldsLength
operator|!=
literal|null
condition|)
block|{
name|String
index|[]
name|outputArray
init|=
name|fieldsLength
operator|.
name|split
argument_list|(
literal|","
argument_list|)
decl_stmt|;
name|outputFieldsLengthArray
operator|=
operator|new
name|Integer
index|[
name|outputArray
operator|.
name|length
index|]
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|outputArray
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|String
name|str
init|=
name|outputArray
index|[
name|i
index|]
decl_stmt|;
name|outputFieldsLengthArray
index|[
name|i
index|]
operator|=
name|Integer
operator|.
name|parseInt
argument_list|(
name|str
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

