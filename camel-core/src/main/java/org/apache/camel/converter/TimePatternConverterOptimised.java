begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.converter
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|converter
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

begin_comment
comment|/**  * Optimised {@link TimePatternConverter}  */
end_comment

begin_class
DECL|class|TimePatternConverterOptimised
specifier|public
specifier|final
class|class
name|TimePatternConverterOptimised
block|{
DECL|method|TimePatternConverterOptimised ()
specifier|private
name|TimePatternConverterOptimised
parameter_list|()
block|{     }
DECL|method|convertTo (final Class<?> type, final Exchange exchange, final Object value)
specifier|public
specifier|static
name|Object
name|convertTo
parameter_list|(
specifier|final
name|Class
argument_list|<
name|?
argument_list|>
name|type
parameter_list|,
specifier|final
name|Exchange
name|exchange
parameter_list|,
specifier|final
name|Object
name|value
parameter_list|)
throws|throws
name|Exception
block|{
comment|// special for String -> long where we support time patterns
if|if
condition|(
name|type
operator|==
name|long
operator|.
name|class
operator|||
name|type
operator|==
name|Long
operator|.
name|class
condition|)
block|{
name|Class
name|fromType
init|=
name|value
operator|.
name|getClass
argument_list|()
decl_stmt|;
if|if
condition|(
name|fromType
operator|==
name|String
operator|.
name|class
condition|)
block|{
return|return
name|TimePatternConverter
operator|.
name|toMilliSeconds
argument_list|(
name|value
operator|.
name|toString
argument_list|()
argument_list|)
return|;
block|}
block|}
comment|// no optimised type converter found
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

