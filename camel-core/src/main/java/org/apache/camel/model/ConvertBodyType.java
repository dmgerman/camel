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
name|java
operator|.
name|util
operator|.
name|Collections
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
name|XmlAttribute
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
name|XmlRootElement
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
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|Processor
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
name|processor
operator|.
name|ConvertBodyProcessor
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
name|ObjectHelper
import|;
end_import

begin_class
annotation|@
name|XmlRootElement
argument_list|(
name|name
operator|=
literal|"convertBodyTo"
argument_list|)
annotation|@
name|XmlAccessorType
argument_list|(
name|XmlAccessType
operator|.
name|FIELD
argument_list|)
DECL|class|ConvertBodyType
specifier|public
class|class
name|ConvertBodyType
extends|extends
name|ProcessorType
argument_list|<
name|ProcessorType
argument_list|>
block|{
annotation|@
name|XmlAttribute
DECL|field|type
specifier|private
name|String
name|type
decl_stmt|;
annotation|@
name|XmlTransient
DECL|field|typeClass
specifier|private
name|Class
name|typeClass
decl_stmt|;
DECL|method|ConvertBodyType ()
specifier|public
name|ConvertBodyType
parameter_list|()
block|{     }
DECL|method|ConvertBodyType (String type)
specifier|public
name|ConvertBodyType
parameter_list|(
name|String
name|type
parameter_list|)
block|{
name|setType
argument_list|(
name|type
argument_list|)
expr_stmt|;
block|}
DECL|method|ConvertBodyType (Class typeClass)
specifier|public
name|ConvertBodyType
parameter_list|(
name|Class
name|typeClass
parameter_list|)
block|{
name|setTypeClass
argument_list|(
name|typeClass
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"convertBodyTo[ "
operator|+
name|getType
argument_list|()
operator|+
literal|"]"
return|;
block|}
annotation|@
name|Override
DECL|method|createProcessor (RouteContext routeContext)
specifier|public
name|Processor
name|createProcessor
parameter_list|(
name|RouteContext
name|routeContext
parameter_list|)
throws|throws
name|Exception
block|{
return|return
operator|new
name|ConvertBodyProcessor
argument_list|(
name|getTypeClass
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|getOutputs ()
specifier|public
name|List
argument_list|<
name|ProcessorType
argument_list|<
name|?
argument_list|>
argument_list|>
name|getOutputs
parameter_list|()
block|{
return|return
name|Collections
operator|.
name|EMPTY_LIST
return|;
block|}
DECL|method|createTypeClass ()
specifier|protected
name|Class
name|createTypeClass
parameter_list|()
block|{
return|return
name|ObjectHelper
operator|.
name|loadClass
argument_list|(
name|getType
argument_list|()
argument_list|,
name|getClass
argument_list|()
operator|.
name|getClassLoader
argument_list|()
argument_list|)
return|;
block|}
DECL|method|setType (String type)
specifier|public
name|void
name|setType
parameter_list|(
name|String
name|type
parameter_list|)
block|{
name|this
operator|.
name|type
operator|=
name|type
expr_stmt|;
block|}
DECL|method|getType ()
specifier|public
name|String
name|getType
parameter_list|()
block|{
return|return
name|type
return|;
block|}
DECL|method|setTypeClass (Class typeClass)
specifier|public
name|void
name|setTypeClass
parameter_list|(
name|Class
name|typeClass
parameter_list|)
block|{
name|this
operator|.
name|typeClass
operator|=
name|typeClass
expr_stmt|;
block|}
DECL|method|getTypeClass ()
specifier|public
name|Class
name|getTypeClass
parameter_list|()
block|{
if|if
condition|(
name|typeClass
operator|==
literal|null
condition|)
block|{
name|setTypeClass
argument_list|(
name|createTypeClass
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|typeClass
return|;
block|}
block|}
end_class

end_unit

