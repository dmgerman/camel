begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.iec60870.server
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|iec60870
operator|.
name|server
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
name|eclipse
operator|.
name|neoscada
operator|.
name|protocol
operator|.
name|iec60870
operator|.
name|asdu
operator|.
name|types
operator|.
name|Value
import|;
end_import

begin_class
DECL|class|ServerProducer
specifier|public
class|class
name|ServerProducer
extends|extends
name|DefaultProducer
block|{
DECL|field|endpoint
specifier|private
specifier|final
name|ServerEndpoint
name|endpoint
decl_stmt|;
DECL|field|server
specifier|private
specifier|final
name|ServerInstance
name|server
decl_stmt|;
DECL|method|ServerProducer (final ServerEndpoint endpoint, final ServerInstance server)
specifier|public
name|ServerProducer
parameter_list|(
specifier|final
name|ServerEndpoint
name|endpoint
parameter_list|,
specifier|final
name|ServerInstance
name|server
parameter_list|)
block|{
name|super
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
name|this
operator|.
name|endpoint
operator|=
name|endpoint
expr_stmt|;
name|this
operator|.
name|server
operator|=
name|server
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|process (final Exchange exchange)
specifier|public
name|void
name|process
parameter_list|(
specifier|final
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
specifier|final
name|Value
argument_list|<
name|?
argument_list|>
name|value
init|=
name|mapToCommand
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
name|this
operator|.
name|server
operator|.
name|notifyValue
argument_list|(
name|this
operator|.
name|endpoint
operator|.
name|getAddress
argument_list|()
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
DECL|method|mapToCommand (final Exchange exchange)
specifier|private
name|Value
argument_list|<
name|?
argument_list|>
name|mapToCommand
parameter_list|(
specifier|final
name|Exchange
name|exchange
parameter_list|)
block|{
specifier|final
name|Object
name|body
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
decl_stmt|;
if|if
condition|(
name|body
operator|instanceof
name|Value
argument_list|<
name|?
argument_list|>
condition|)
block|{
return|return
operator|(
name|Value
argument_list|<
name|?
argument_list|>
operator|)
name|body
return|;
block|}
if|if
condition|(
name|body
operator|instanceof
name|Float
operator|||
name|body
operator|instanceof
name|Double
condition|)
block|{
return|return
name|Value
operator|.
name|ok
argument_list|(
operator|(
operator|(
name|Number
operator|)
name|body
operator|)
operator|.
name|floatValue
argument_list|()
argument_list|)
return|;
block|}
if|if
condition|(
name|body
operator|instanceof
name|Boolean
condition|)
block|{
return|return
name|Value
operator|.
name|ok
argument_list|(
operator|(
name|Boolean
operator|)
name|body
argument_list|)
return|;
block|}
if|if
condition|(
name|body
operator|instanceof
name|Short
operator|||
name|body
operator|instanceof
name|Byte
operator|||
name|body
operator|instanceof
name|Integer
operator|||
name|body
operator|instanceof
name|Long
condition|)
block|{
return|return
name|convertToShort
argument_list|(
operator|(
operator|(
name|Number
operator|)
name|body
operator|)
operator|.
name|longValue
argument_list|()
argument_list|)
return|;
block|}
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Unable to map body to a value: "
operator|+
name|body
argument_list|)
throw|;
block|}
DECL|method|convertToShort (final long value)
specifier|private
name|Value
argument_list|<
name|?
argument_list|>
name|convertToShort
parameter_list|(
specifier|final
name|long
name|value
parameter_list|)
block|{
if|if
condition|(
name|value
argument_list|<
name|Short
operator|.
name|MIN_VALUE
operator|||
name|value
argument_list|>
name|Short
operator|.
name|MAX_VALUE
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"Value must be between %s and %s"
argument_list|,
name|Short
operator|.
name|MIN_VALUE
argument_list|,
name|Short
operator|.
name|MAX_VALUE
argument_list|)
argument_list|)
throw|;
block|}
return|return
name|Value
operator|.
name|ok
argument_list|(
operator|(
name|short
operator|)
name|value
argument_list|)
return|;
block|}
block|}
end_class

end_unit

