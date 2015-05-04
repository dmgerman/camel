begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.converter.dozer
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|converter
operator|.
name|dozer
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
name|TypeConversionException
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
name|TypeConverter
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
name|TypeConverterSupport
import|;
end_import

begin_import
import|import
name|org
operator|.
name|dozer
operator|.
name|DozerBeanMapper
import|;
end_import

begin_import
import|import
name|org
operator|.
name|dozer
operator|.
name|Mapper
import|;
end_import

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
comment|/**  *<code>DozerTypeConverter</code> is a standard {@link TypeConverter} that  * delegates to a {@link Mapper} from the Dozer framework to convert between  * types.<code>DozerTypeConverter</code>s are created and installed into a  * {@link CamelContext} by an instance of {@link DozerTypeConverterLoader}.  *<p>  * See<a href="http://dozer.sourceforge.net">dozer project page</a> or more information on configuring Dozer  *  * @see DozerTypeConverterLoader  */
end_comment

begin_class
DECL|class|DozerTypeConverter
specifier|public
class|class
name|DozerTypeConverter
extends|extends
name|TypeConverterSupport
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|DozerTypeConverter
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|mapper
specifier|private
specifier|final
name|DozerBeanMapper
name|mapper
decl_stmt|;
DECL|method|DozerTypeConverter (DozerBeanMapper mapper)
specifier|public
name|DozerTypeConverter
parameter_list|(
name|DozerBeanMapper
name|mapper
parameter_list|)
block|{
name|this
operator|.
name|mapper
operator|=
name|mapper
expr_stmt|;
block|}
DECL|method|getMapper ()
specifier|public
name|DozerBeanMapper
name|getMapper
parameter_list|()
block|{
return|return
name|mapper
return|;
block|}
annotation|@
name|Override
DECL|method|convertTo (Class<T> type, Exchange exchange, Object value)
specifier|public
parameter_list|<
name|T
parameter_list|>
name|T
name|convertTo
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|,
name|Exchange
name|exchange
parameter_list|,
name|Object
name|value
parameter_list|)
throws|throws
name|TypeConversionException
block|{
comment|// if the exchange is null, we have no chance to ensure that the TCCL is the one from the CamelContext
if|if
condition|(
name|exchange
operator|==
literal|null
condition|)
block|{
return|return
name|mapper
operator|.
name|map
argument_list|(
name|value
argument_list|,
name|type
argument_list|)
return|;
block|}
comment|// otherwise, we ensure that the TCCL is the correct one
name|T
name|answer
decl_stmt|;
name|ClassLoader
name|prev
init|=
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|getContextClassLoader
argument_list|()
decl_stmt|;
name|ClassLoader
name|contextCl
init|=
name|exchange
operator|.
name|getContext
argument_list|()
operator|.
name|getApplicationContextClassLoader
argument_list|()
decl_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"Switching TCCL to: {}."
argument_list|,
name|contextCl
argument_list|)
expr_stmt|;
try|try
block|{
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|setContextClassLoader
argument_list|(
name|contextCl
argument_list|)
expr_stmt|;
name|answer
operator|=
name|mapper
operator|.
name|map
argument_list|(
name|value
argument_list|,
name|type
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Restored TCCL to: {}."
argument_list|,
name|prev
argument_list|)
expr_stmt|;
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|setContextClassLoader
argument_list|(
name|prev
argument_list|)
expr_stmt|;
block|}
return|return
name|answer
return|;
block|}
block|}
end_class

end_unit

