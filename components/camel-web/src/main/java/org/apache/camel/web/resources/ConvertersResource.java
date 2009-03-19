begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.web.resources
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|web
operator|.
name|resources
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
name|java
operator|.
name|util
operator|.
name|Set
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|TreeMap
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|Path
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|PathParam
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
name|converter
operator|.
name|DefaultTypeConverter
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|Log
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|LogFactory
import|;
end_import

begin_comment
comment|/**  * @version $Revision: 1.1 $  */
end_comment

begin_class
DECL|class|ConvertersResource
specifier|public
class|class
name|ConvertersResource
extends|extends
name|CamelChildResourceSupport
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
specifier|transient
name|Log
name|LOG
init|=
name|LogFactory
operator|.
name|getLog
argument_list|(
name|ConvertersResource
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|method|ConvertersResource (CamelContextResource contextResource)
specifier|public
name|ConvertersResource
parameter_list|(
name|CamelContextResource
name|contextResource
parameter_list|)
block|{
name|super
argument_list|(
name|contextResource
argument_list|)
expr_stmt|;
block|}
DECL|method|getFromClassTypes ()
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|Class
argument_list|>
name|getFromClassTypes
parameter_list|()
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Class
argument_list|>
name|answer
init|=
operator|new
name|TreeMap
argument_list|<
name|String
argument_list|,
name|Class
argument_list|>
argument_list|()
decl_stmt|;
name|DefaultTypeConverter
name|converter
init|=
name|getDefaultTypeConverter
argument_list|()
decl_stmt|;
if|if
condition|(
name|converter
operator|!=
literal|null
condition|)
block|{
name|Set
argument_list|<
name|Class
argument_list|>
name|classes
init|=
name|converter
operator|.
name|getFromClassMappings
argument_list|()
decl_stmt|;
for|for
control|(
name|Class
name|aClass
range|:
name|classes
control|)
block|{
name|String
name|name
init|=
name|nameOf
argument_list|(
name|aClass
argument_list|)
decl_stmt|;
name|answer
operator|.
name|put
argument_list|(
name|name
argument_list|,
name|aClass
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|answer
return|;
block|}
comment|/**      * Returns type converters from the given type      */
annotation|@
name|Path
argument_list|(
literal|"{type}"
argument_list|)
comment|/*     TODO this doesn't work in JAX-RS yet      public ConvertersFromResource getConvertersFrom(@PathParam("type") Class type) { */
DECL|method|getConvertersFrom (@athParamR) String typeName)
specifier|public
name|ConvertersFromResource
name|getConvertersFrom
parameter_list|(
annotation|@
name|PathParam
argument_list|(
literal|"type"
argument_list|)
name|String
name|typeName
parameter_list|)
block|{
name|Class
name|type
init|=
name|getCamelContext
argument_list|()
operator|.
name|getClassResolver
argument_list|()
operator|.
name|resolveClass
argument_list|(
name|typeName
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
return|return
literal|null
return|;
block|}
return|return
operator|new
name|ConvertersFromResource
argument_list|(
name|getContextResource
argument_list|()
argument_list|,
name|type
argument_list|)
return|;
block|}
DECL|method|nameOf (Class aClass)
specifier|public
specifier|static
name|String
name|nameOf
parameter_list|(
name|Class
name|aClass
parameter_list|)
block|{
return|return
name|aClass
operator|.
name|getCanonicalName
argument_list|()
return|;
block|}
block|}
end_class

end_unit

