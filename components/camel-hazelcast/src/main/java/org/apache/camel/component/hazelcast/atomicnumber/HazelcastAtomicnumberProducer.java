begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.hazelcast.atomicnumber
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|hazelcast
operator|.
name|atomicnumber
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
name|com
operator|.
name|hazelcast
operator|.
name|core
operator|.
name|HazelcastInstance
import|;
end_import

begin_import
import|import
name|com
operator|.
name|hazelcast
operator|.
name|core
operator|.
name|IAtomicLong
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
name|component
operator|.
name|hazelcast
operator|.
name|HazelcastComponentHelper
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
name|hazelcast
operator|.
name|HazelcastConstants
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
name|hazelcast
operator|.
name|HazelcastDefaultEndpoint
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
name|hazelcast
operator|.
name|HazelcastDefaultProducer
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
name|hazelcast
operator|.
name|HazelcastOperation
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

begin_class
DECL|class|HazelcastAtomicnumberProducer
specifier|public
class|class
name|HazelcastAtomicnumberProducer
extends|extends
name|HazelcastDefaultProducer
block|{
DECL|field|atomicnumber
specifier|private
specifier|final
name|IAtomicLong
name|atomicnumber
decl_stmt|;
DECL|method|HazelcastAtomicnumberProducer (HazelcastInstance hazelcastInstance, HazelcastDefaultEndpoint endpoint, String cacheName)
specifier|public
name|HazelcastAtomicnumberProducer
parameter_list|(
name|HazelcastInstance
name|hazelcastInstance
parameter_list|,
name|HazelcastDefaultEndpoint
name|endpoint
parameter_list|,
name|String
name|cacheName
parameter_list|)
block|{
name|super
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
name|this
operator|.
name|atomicnumber
operator|=
name|hazelcastInstance
operator|.
name|getAtomicLong
argument_list|(
name|cacheName
argument_list|)
expr_stmt|;
block|}
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
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|headers
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeaders
argument_list|()
decl_stmt|;
name|long
name|expectedValue
init|=
literal|0L
decl_stmt|;
if|if
condition|(
name|headers
operator|.
name|containsKey
argument_list|(
name|HazelcastConstants
operator|.
name|EXPECTED_VALUE
argument_list|)
condition|)
block|{
name|expectedValue
operator|=
operator|(
name|long
operator|)
name|headers
operator|.
name|get
argument_list|(
name|HazelcastConstants
operator|.
name|EXPECTED_VALUE
argument_list|)
expr_stmt|;
block|}
name|HazelcastOperation
name|operation
init|=
name|lookupOperation
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
switch|switch
condition|(
name|operation
condition|)
block|{
case|case
name|INCREMENT
case|:
name|this
operator|.
name|increment
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
break|break;
case|case
name|DECREMENT
case|:
name|this
operator|.
name|decrement
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
break|break;
case|case
name|COMPARE_AND_SET
case|:
name|this
operator|.
name|compare
argument_list|(
name|expectedValue
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
break|break;
case|case
name|GET_AND_ADD
case|:
name|this
operator|.
name|getAndAdd
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
break|break;
case|case
name|SET_VALUE
case|:
name|this
operator|.
name|set
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
break|break;
case|case
name|GET
case|:
name|this
operator|.
name|get
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
break|break;
case|case
name|DESTROY
case|:
name|this
operator|.
name|destroy
argument_list|()
expr_stmt|;
break|break;
default|default:
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"The value '%s' is not allowed for parameter '%s' on the ATOMICNUMBER."
argument_list|,
name|operation
argument_list|,
name|HazelcastConstants
operator|.
name|OPERATION
argument_list|)
argument_list|)
throw|;
block|}
comment|// finally copy headers
name|HazelcastComponentHelper
operator|.
name|copyHeaders
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
DECL|method|get (Exchange exchange)
specifier|private
name|void
name|get
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setBody
argument_list|(
name|this
operator|.
name|atomicnumber
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|increment (Exchange exchange)
specifier|private
name|void
name|increment
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setBody
argument_list|(
name|this
operator|.
name|atomicnumber
operator|.
name|incrementAndGet
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|decrement (Exchange exchange)
specifier|private
name|void
name|decrement
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setBody
argument_list|(
name|this
operator|.
name|atomicnumber
operator|.
name|decrementAndGet
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|compare (long expected, Exchange exchange)
specifier|private
name|void
name|compare
parameter_list|(
name|long
name|expected
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
block|{
name|long
name|update
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|Long
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|ObjectHelper
operator|.
name|isEmpty
argument_list|(
name|expected
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Expected value must be specified"
argument_list|)
throw|;
block|}
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setBody
argument_list|(
name|this
operator|.
name|atomicnumber
operator|.
name|compareAndSet
argument_list|(
name|expected
argument_list|,
name|update
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|getAndAdd (Exchange exchange)
specifier|private
name|void
name|getAndAdd
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|long
name|delta
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|Long
operator|.
name|class
argument_list|)
decl_stmt|;
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setBody
argument_list|(
name|this
operator|.
name|atomicnumber
operator|.
name|getAndAdd
argument_list|(
name|delta
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|set (Exchange exchange)
specifier|private
name|void
name|set
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|this
operator|.
name|atomicnumber
operator|.
name|set
argument_list|(
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|Long
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|destroy ()
specifier|private
name|void
name|destroy
parameter_list|()
block|{
name|this
operator|.
name|atomicnumber
operator|.
name|destroy
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

