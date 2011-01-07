begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.model
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|model
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

begin_comment
comment|/**  * Represents the base XML type for DataFormat.  *  * @version $Revision$  */
end_comment

begin_class
annotation|@
name|XmlType
argument_list|(
name|name
operator|=
literal|"dataFormat"
argument_list|)
annotation|@
name|XmlAccessorType
argument_list|(
name|XmlAccessType
operator|.
name|FIELD
argument_list|)
DECL|class|DataFormatDefinition
specifier|public
class|class
name|DataFormatDefinition
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
DECL|field|dataFormatName
specifier|private
name|String
name|dataFormatName
decl_stmt|;
DECL|method|DataFormatDefinition ()
specifier|public
name|DataFormatDefinition
parameter_list|()
block|{     }
DECL|method|DataFormatDefinition (DataFormat dataFormat)
specifier|public
name|DataFormatDefinition
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
DECL|method|DataFormatDefinition (String dataFormatName)
specifier|protected
name|DataFormatDefinition
parameter_list|(
name|String
name|dataFormatName
parameter_list|)
block|{
name|this
operator|.
name|dataFormatName
operator|=
name|dataFormatName
expr_stmt|;
block|}
comment|/**      * Factory method to create the data format      *      * @param routeContext route context      * @param type         the data format type      * @param ref          reference to lookup for a data format      * @return the data format or null if not possible to create      */
DECL|method|getDataFormat (RouteContext routeContext, DataFormatDefinition type, String ref)
specifier|public
specifier|static
name|DataFormat
name|getDataFormat
parameter_list|(
name|RouteContext
name|routeContext
parameter_list|,
name|DataFormatDefinition
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
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|ref
argument_list|,
literal|"ref or type"
argument_list|)
expr_stmt|;
comment|// try to let resolver see if it can resolve it, its not always possible
name|type
operator|=
name|routeContext
operator|.
name|getCamelContext
argument_list|()
operator|.
name|resolveDataFormatDefinition
argument_list|(
name|ref
argument_list|)
expr_stmt|;
if|if
condition|(
name|type
operator|!=
literal|null
condition|)
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
name|DataFormat
name|dataFormat
init|=
name|routeContext
operator|.
name|getCamelContext
argument_list|()
operator|.
name|resolveDataFormat
argument_list|(
name|ref
argument_list|)
decl_stmt|;
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
literal|"Data format could not be created. Ensure that the dataformat is valid and the associated camel component is present on the classpath"
argument_list|)
throw|;
block|}
else|else
block|{
name|configureDataFormat
argument_list|(
name|dataFormat
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|dataFormat
return|;
block|}
comment|/**      * Factory method to create the data format instance      */
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
name|dataFormatName
operator|!=
literal|null
condition|)
block|{
return|return
name|routeContext
operator|.
name|getCamelContext
argument_list|()
operator|.
name|resolveDataFormat
argument_list|(
name|dataFormatName
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
DECL|method|getDataFormatName ()
specifier|public
name|String
name|getDataFormatName
parameter_list|()
block|{
return|return
name|dataFormatName
return|;
block|}
DECL|method|setDataFormatName (String dataFormatName)
specifier|public
name|void
name|setDataFormatName
parameter_list|(
name|String
name|dataFormatName
parameter_list|)
block|{
name|this
operator|.
name|dataFormatName
operator|=
name|dataFormatName
expr_stmt|;
block|}
DECL|method|getDataFormat ()
specifier|public
name|DataFormat
name|getDataFormat
parameter_list|()
block|{
return|return
name|dataFormat
return|;
block|}
DECL|method|setDataFormat (DataFormat dataFormat)
specifier|public
name|void
name|setDataFormat
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
DECL|method|getShortName ()
specifier|public
name|String
name|getShortName
parameter_list|()
block|{
name|String
name|name
init|=
name|getClass
argument_list|()
operator|.
name|getSimpleName
argument_list|()
decl_stmt|;
if|if
condition|(
name|name
operator|.
name|endsWith
argument_list|(
literal|"DataFormat"
argument_list|)
condition|)
block|{
name|name
operator|=
name|name
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|name
operator|.
name|indexOf
argument_list|(
literal|"DataFormat"
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|name
return|;
block|}
block|}
end_class

end_unit

