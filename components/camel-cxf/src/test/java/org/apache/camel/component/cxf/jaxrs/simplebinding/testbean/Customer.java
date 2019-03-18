begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.cxf.jaxrs.simplebinding.testbean
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|cxf
operator|.
name|jaxrs
operator|.
name|simplebinding
operator|.
name|testbean
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
name|XmlRootElement
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
literal|"Customer"
argument_list|)
DECL|class|Customer
specifier|public
class|class
name|Customer
block|{
DECL|field|id
specifier|private
name|long
name|id
decl_stmt|;
DECL|field|name
specifier|private
name|String
name|name
decl_stmt|;
DECL|method|Customer ()
specifier|public
name|Customer
parameter_list|()
block|{     }
DECL|method|Customer (long id, String name)
specifier|public
name|Customer
parameter_list|(
name|long
name|id
parameter_list|,
name|String
name|name
parameter_list|)
block|{
name|setId
argument_list|(
name|id
argument_list|)
expr_stmt|;
name|setName
argument_list|(
name|name
argument_list|)
expr_stmt|;
block|}
DECL|method|getId ()
specifier|public
name|long
name|getId
parameter_list|()
block|{
return|return
name|id
return|;
block|}
DECL|method|setId (long id)
specifier|public
name|void
name|setId
parameter_list|(
name|long
name|id
parameter_list|)
block|{
name|this
operator|.
name|id
operator|=
name|id
expr_stmt|;
block|}
DECL|method|getName ()
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
name|name
return|;
block|}
DECL|method|setName (String name)
specifier|public
name|void
name|setName
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|this
operator|.
name|name
operator|=
name|name
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|hashCode ()
specifier|public
name|int
name|hashCode
parameter_list|()
block|{
specifier|final
name|int
name|prime
init|=
literal|31
decl_stmt|;
name|int
name|result
init|=
literal|1
decl_stmt|;
name|result
operator|=
name|prime
operator|*
name|result
operator|+
call|(
name|int
call|)
argument_list|(
name|id
operator|^
operator|(
name|id
operator|>>>
literal|32
operator|)
argument_list|)
expr_stmt|;
name|result
operator|=
name|prime
operator|*
name|result
operator|+
operator|(
operator|(
name|name
operator|==
literal|null
operator|)
condition|?
literal|0
else|:
name|name
operator|.
name|hashCode
argument_list|()
operator|)
expr_stmt|;
return|return
name|result
return|;
block|}
annotation|@
name|Override
DECL|method|equals (Object obj)
specifier|public
name|boolean
name|equals
parameter_list|(
name|Object
name|obj
parameter_list|)
block|{
if|if
condition|(
operator|!
operator|(
name|obj
operator|instanceof
name|Customer
operator|)
condition|)
block|{
return|return
literal|false
return|;
block|}
if|if
condition|(
name|this
operator|==
name|obj
condition|)
block|{
return|return
literal|true
return|;
block|}
name|Customer
name|other
init|=
operator|(
name|Customer
operator|)
name|obj
decl_stmt|;
return|return
name|id
operator|==
name|other
operator|.
name|id
operator|&&
name|ObjectHelper
operator|.
name|equal
argument_list|(
name|name
argument_list|,
name|other
operator|.
name|name
argument_list|)
return|;
block|}
block|}
end_class

end_unit

