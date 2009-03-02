begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.model.dataformat
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|model
operator|.
name|dataformat
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlAccessType
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlAccessorType
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlTransient
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlType
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
name|IdentifiedType
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
name|RouteContext
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
name|IntrospectionSupport
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
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|util
operator|.
name|ObjectHelper
operator|.
name|notNull
import|;
end_import

begin_comment
comment|/**  * Represents the base XML type for DataFormat.  *  * @version $Revision$  */
end_comment

begin_class
annotation|@
name|XmlType
argument_list|(
name|name
operator|=
literal|"dataFormatType"
argument_list|)
annotation|@
name|XmlAccessorType
argument_list|(
name|XmlAccessType
operator|.
name|FIELD
argument_list|)
DECL|class|DataFormatType
specifier|public
class|class
name|DataFormatType
extends|extends
name|IdentifiedType
block|{
annotation|@
name|XmlTransient
DECL|field|dataFormat
specifier|private
name|DataFormat
name|dataFormat
decl_stmt|;
annotation|@
name|XmlTransient
DECL|field|dataFormatTypeName
specifier|private
name|String
name|dataFormatTypeName
decl_stmt|;
DECL|method|DataFormatType ()
specifier|public
name|DataFormatType
parameter_list|()
block|{     }
DECL|method|DataFormatType (DataFormat dataFormat)
specifier|public
name|DataFormatType
parameter_list|(
name|DataFormat
name|dataFormat
parameter_list|)
block|{
name|this
operator|.
name|dataFormat
operator|=
name|dataFormat
expr_stmt|;
block|}
DECL|method|DataFormatType (String dataFormatTypeName)
specifier|protected
name|DataFormatType
parameter_list|(
name|String
name|dataFormatTypeName
parameter_list|)
block|{
name|this
operator|.
name|dataFormatTypeName
operator|=
name|dataFormatTypeName
expr_stmt|;
block|}
comment|/**      * Factory method to create the data format      * @param routeContext route context      * @param type the data format type      * @param ref  reference to lookup for a data format      * @return the data format or null if not possible to create      */
DECL|method|getDataFormat (RouteContext routeContext, DataFormatType type, String ref)
specifier|public
specifier|static
name|DataFormat
name|getDataFormat
parameter_list|(
name|RouteContext
name|routeContext
parameter_list|,
name|DataFormatType
name|type
parameter_list|,
name|String
name|ref
parameter_list|)
block|{
if|if
condition|(
name|type
operator|==
literal|null
condition|)
block|{
name|notNull
argument_list|(
name|ref
argument_list|,
literal|"ref or dataFormatType"
argument_list|)
expr_stmt|;
name|DataFormat
name|dataFormat
init|=
name|lookup
argument_list|(
name|routeContext
argument_list|,
name|ref
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
comment|// lookup type and create the data format from it
name|type
operator|=
name|lookup
argument_list|(
name|routeContext
argument_list|,
name|ref
argument_list|,
name|DataFormatType
operator|.
name|class
argument_list|)
expr_stmt|;
if|if
condition|(
name|type
operator|==
literal|null
condition|)
block|{
name|type
operator|=
name|routeContext
operator|.
name|getDataFormat
argument_list|(
name|ref
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
name|dataFormat
operator|=
name|type
operator|.
name|createDataFormat
argument_list|(
name|routeContext
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|dataFormat
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Cannot find data format in registry with ref: "
operator|+
name|ref
argument_list|)
throw|;
block|}
return|return
name|dataFormat
return|;
block|}
else|else
block|{
return|return
name|type
operator|.
name|getDataFormat
argument_list|(
name|routeContext
argument_list|)
return|;
block|}
block|}
DECL|method|lookup (RouteContext routeContext, String ref, Class<T> type)
specifier|private
specifier|static
parameter_list|<
name|T
parameter_list|>
name|T
name|lookup
parameter_list|(
name|RouteContext
name|routeContext
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
name|routeContext
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
DECL|method|getDataFormat (RouteContext routeContext)
specifier|public
name|DataFormat
name|getDataFormat
parameter_list|(
name|RouteContext
name|routeContext
parameter_list|)
block|{
if|if
condition|(
name|dataFormat
operator|==
literal|null
condition|)
block|{
name|dataFormat
operator|=
name|createDataFormat
argument_list|(
name|routeContext
argument_list|)
expr_stmt|;
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|dataFormat
argument_list|,
literal|"dataFormat"
argument_list|)
expr_stmt|;
name|configureDataFormat
argument_list|(
name|dataFormat
argument_list|)
expr_stmt|;
block|}
return|return
name|dataFormat
return|;
block|}
comment|/**      * Factory method to create the data format instance      */
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|createDataFormat (RouteContext routeContext)
specifier|protected
name|DataFormat
name|createDataFormat
parameter_list|(
name|RouteContext
name|routeContext
parameter_list|)
block|{
if|if
condition|(
name|dataFormatTypeName
operator|!=
literal|null
condition|)
block|{
name|Class
name|type
init|=
name|ObjectHelper
operator|.
name|loadClass
argument_list|(
name|dataFormatTypeName
argument_list|,
name|getClass
argument_list|()
operator|.
name|getClassLoader
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|type
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"The class "
operator|+
name|dataFormatTypeName
operator|+
literal|" is not on the classpath! Cannot use the dataFormat "
operator|+
name|this
argument_list|)
throw|;
block|}
return|return
operator|(
name|DataFormat
operator|)
name|ObjectHelper
operator|.
name|newInstance
argument_list|(
name|type
argument_list|)
return|;
block|}
return|return
literal|null
return|;
block|}
comment|/**      * Allows derived classes to customize the data format      */
DECL|method|configureDataFormat (DataFormat dataFormat)
specifier|protected
name|void
name|configureDataFormat
parameter_list|(
name|DataFormat
name|dataFormat
parameter_list|)
block|{     }
comment|/**      * Sets a named property on the data format instance using introspection      */
DECL|method|setProperty (Object bean, String name, Object value)
specifier|protected
name|void
name|setProperty
parameter_list|(
name|Object
name|bean
parameter_list|,
name|String
name|name
parameter_list|,
name|Object
name|value
parameter_list|)
block|{
try|try
block|{
name|IntrospectionSupport
operator|.
name|setProperty
argument_list|(
name|bean
argument_list|,
name|name
argument_list|,
name|value
argument_list|)
expr_stmt|;
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
literal|"Failed to set property: "
operator|+
name|name
operator|+
literal|" on: "
operator|+
name|bean
operator|+
literal|". Reason: "
operator|+
name|e
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

