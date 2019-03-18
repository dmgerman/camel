begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.thrift.impl
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|thrift
operator|.
name|impl
package|;
end_package

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|ByteBuffer
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
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
name|component
operator|.
name|thrift
operator|.
name|generated
operator|.
name|Calculator
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
name|thrift
operator|.
name|generated
operator|.
name|InvalidOperation
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
name|thrift
operator|.
name|generated
operator|.
name|Work
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|thrift
operator|.
name|TException
import|;
end_import

begin_comment
comment|/**  * Test Thrift Calculator blocking server implementation  */
end_comment

begin_class
DECL|class|CalculatorSyncServerImpl
specifier|public
class|class
name|CalculatorSyncServerImpl
implements|implements
name|Calculator
operator|.
name|Iface
block|{
annotation|@
name|Override
DECL|method|ping ()
specifier|public
name|void
name|ping
parameter_list|()
throws|throws
name|TException
block|{     }
annotation|@
name|Override
DECL|method|add (int num1, int num2)
specifier|public
name|int
name|add
parameter_list|(
name|int
name|num1
parameter_list|,
name|int
name|num2
parameter_list|)
throws|throws
name|TException
block|{
return|return
name|num1
operator|+
name|num2
return|;
block|}
annotation|@
name|Override
DECL|method|calculate (int logId, Work work)
specifier|public
name|int
name|calculate
parameter_list|(
name|int
name|logId
parameter_list|,
name|Work
name|work
parameter_list|)
throws|throws
name|InvalidOperation
throws|,
name|TException
block|{
name|int
name|val
init|=
literal|0
decl_stmt|;
switch|switch
condition|(
name|work
operator|.
name|op
condition|)
block|{
case|case
name|ADD
case|:
name|val
operator|=
name|work
operator|.
name|num1
operator|+
name|work
operator|.
name|num2
expr_stmt|;
break|break;
case|case
name|SUBTRACT
case|:
name|val
operator|=
name|work
operator|.
name|num1
operator|-
name|work
operator|.
name|num2
expr_stmt|;
break|break;
case|case
name|MULTIPLY
case|:
name|val
operator|=
name|work
operator|.
name|num1
operator|*
name|work
operator|.
name|num2
expr_stmt|;
break|break;
case|case
name|DIVIDE
case|:
if|if
condition|(
name|work
operator|.
name|num2
operator|==
literal|0
condition|)
block|{
name|InvalidOperation
name|io
init|=
operator|new
name|InvalidOperation
argument_list|()
decl_stmt|;
name|io
operator|.
name|whatOp
operator|=
name|work
operator|.
name|op
operator|.
name|getValue
argument_list|()
expr_stmt|;
name|io
operator|.
name|why
operator|=
literal|"Cannot divide by 0"
expr_stmt|;
throw|throw
name|io
throw|;
block|}
name|val
operator|=
name|work
operator|.
name|num1
operator|/
name|work
operator|.
name|num2
expr_stmt|;
break|break;
default|default:
name|InvalidOperation
name|io
init|=
operator|new
name|InvalidOperation
argument_list|()
decl_stmt|;
name|io
operator|.
name|whatOp
operator|=
name|work
operator|.
name|op
operator|.
name|getValue
argument_list|()
expr_stmt|;
name|io
operator|.
name|why
operator|=
literal|"Unknown operation"
expr_stmt|;
throw|throw
name|io
throw|;
block|}
return|return
name|val
return|;
block|}
annotation|@
name|Override
DECL|method|zip ()
specifier|public
name|void
name|zip
parameter_list|()
throws|throws
name|TException
block|{     }
annotation|@
name|Override
DECL|method|echo (Work w)
specifier|public
name|Work
name|echo
parameter_list|(
name|Work
name|w
parameter_list|)
throws|throws
name|TException
block|{
return|return
name|w
operator|.
name|deepCopy
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|alltypes (boolean v1, byte v2, short v3, int v4, long v5, double v6, String v7, ByteBuffer v8, Work v9, List<Integer> v10, Set<String> v11, Map<String, Long> v12)
specifier|public
name|int
name|alltypes
parameter_list|(
name|boolean
name|v1
parameter_list|,
name|byte
name|v2
parameter_list|,
name|short
name|v3
parameter_list|,
name|int
name|v4
parameter_list|,
name|long
name|v5
parameter_list|,
name|double
name|v6
parameter_list|,
name|String
name|v7
parameter_list|,
name|ByteBuffer
name|v8
parameter_list|,
name|Work
name|v9
parameter_list|,
name|List
argument_list|<
name|Integer
argument_list|>
name|v10
parameter_list|,
name|Set
argument_list|<
name|String
argument_list|>
name|v11
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Long
argument_list|>
name|v12
parameter_list|)
throws|throws
name|TException
block|{
return|return
literal|1
return|;
block|}
block|}
end_class

end_unit

