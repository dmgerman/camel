begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.impl
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|impl
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
name|NoFactoryAvailableException
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
name|model
operator|.
name|DataFormatDefinition
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
name|DataFormat
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
name|DataFormatResolver
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
name|FactoryFinder
import|;
end_import

begin_comment
comment|/**  * Default data format resolver  *  * @version   */
end_comment

begin_class
DECL|class|DefaultDataFormatResolver
specifier|public
class|class
name|DefaultDataFormatResolver
implements|implements
name|DataFormatResolver
block|{
DECL|field|DATAFORMAT_RESOURCE_PATH
specifier|public
specifier|static
specifier|final
name|String
name|DATAFORMAT_RESOURCE_PATH
init|=
literal|"META-INF/services/org/apache/camel/dataformat/"
decl_stmt|;
DECL|field|dataformatFactory
specifier|protected
name|FactoryFinder
name|dataformatFactory
decl_stmt|;
DECL|method|resolveDataFormat (String name, CamelContext context)
specifier|public
name|DataFormat
name|resolveDataFormat
parameter_list|(
name|String
name|name
parameter_list|,
name|CamelContext
name|context
parameter_list|)
block|{
name|DataFormat
name|dataFormat
init|=
name|lookup
argument_list|(
name|context
argument_list|,
name|name
argument_list|,
name|DataFormat
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|dataFormat
operator|==
literal|null
condition|)
block|{
name|Class
argument_list|<
name|?
argument_list|>
name|type
init|=
literal|null
decl_stmt|;
try|try
block|{
if|if
condition|(
name|dataformatFactory
operator|==
literal|null
condition|)
block|{
name|dataformatFactory
operator|=
name|context
operator|.
name|getFactoryFinder
argument_list|(
name|DATAFORMAT_RESOURCE_PATH
argument_list|)
expr_stmt|;
block|}
name|type
operator|=
name|dataformatFactory
operator|.
name|findClass
argument_list|(
name|name
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NoFactoryAvailableException
name|e
parameter_list|)
block|{
comment|// ignore
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Invalid URI, no DataFormat registered for scheme: "
operator|+
name|name
argument_list|,
name|e
argument_list|)
throw|;
block|}
if|if
condition|(
name|type
operator|==
literal|null
condition|)
block|{
name|type
operator|=
name|context
operator|.
name|getClassResolver
argument_list|()
operator|.
name|resolveClass
argument_list|(
name|name
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|type
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|DataFormat
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|type
argument_list|)
condition|)
block|{
name|dataFormat
operator|=
operator|(
name|DataFormat
operator|)
name|context
operator|.
name|getInjector
argument_list|()
operator|.
name|newInstance
argument_list|(
name|type
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Resolving dataformat: "
operator|+
name|name
operator|+
literal|" detected type conflict: Not a DataFormat implementation. Found: "
operator|+
name|type
operator|.
name|getName
argument_list|()
argument_list|)
throw|;
block|}
block|}
block|}
return|return
name|dataFormat
return|;
block|}
DECL|method|resolveDataFormatDefinition (String name, CamelContext context)
specifier|public
name|DataFormatDefinition
name|resolveDataFormatDefinition
parameter_list|(
name|String
name|name
parameter_list|,
name|CamelContext
name|context
parameter_list|)
block|{
comment|// lookup type and create the data format from it
name|DataFormatDefinition
name|type
init|=
name|lookup
argument_list|(
name|context
argument_list|,
name|name
argument_list|,
name|DataFormatDefinition
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|type
operator|==
literal|null
operator|&&
name|context
operator|.
name|getDataFormats
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|type
operator|=
name|context
operator|.
name|getDataFormats
argument_list|()
operator|.
name|get
argument_list|(
name|name
argument_list|)
expr_stmt|;
block|}
return|return
name|type
return|;
block|}
DECL|method|lookup (CamelContext context, String ref, Class<T> type)
specifier|private
specifier|static
parameter_list|<
name|T
parameter_list|>
name|T
name|lookup
parameter_list|(
name|CamelContext
name|context
parameter_list|,
name|String
name|ref
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
block|{
try|try
block|{
return|return
name|context
operator|.
name|getRegistry
argument_list|()
operator|.
name|lookup
argument_list|(
name|ref
argument_list|,
name|type
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
comment|// need to ignore not same type and return it as null
return|return
literal|null
return|;
block|}
block|}
block|}
end_class

end_unit

