begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.example.pulsar.server
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|example
operator|.
name|pulsar
operator|.
name|server
package|;
end_package

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_comment
comment|/**  * This is the implementation of the business service.  */
end_comment

begin_class
DECL|class|Treble
specifier|public
class|class
name|Treble
implements|implements
name|Multiplier
block|{
DECL|field|LOGGER
specifier|private
specifier|static
specifier|final
name|Logger
name|LOGGER
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|Treble
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|method|multiply (final int originalNumber)
specifier|public
name|int
name|multiply
parameter_list|(
specifier|final
name|int
name|originalNumber
parameter_list|)
block|{
name|LOGGER
operator|.
name|info
argument_list|(
literal|"Number to multiply received {}"
argument_list|,
name|originalNumber
argument_list|)
expr_stmt|;
return|return
name|originalNumber
operator|*
literal|3
return|;
block|}
block|}
end_class

begin_comment
comment|// END SNIPPET: e1
end_comment

end_unit

