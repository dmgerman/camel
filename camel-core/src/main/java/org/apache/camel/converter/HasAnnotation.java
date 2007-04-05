begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
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
name|java
operator|.
name|lang
operator|.
name|annotation
operator|.
name|Retention
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|annotation
operator|.
name|RetentionPolicy
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|annotation
operator|.
name|Target
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|annotation
operator|.
name|ElementType
import|;
end_import

begin_comment
comment|/**  * Used to indicate that the actual type of a parameter on a converter method must have the given annotation class  * to be applicable. e.g. this annotation could be used on a JAXB converter which only applies to objects with a  * JAXB annotation on them  *  * @version $Revision$  */
end_comment

begin_annotation_defn
annotation|@
name|Retention
argument_list|(
name|RetentionPolicy
operator|.
name|RUNTIME
argument_list|)
annotation|@
name|Target
argument_list|(
block|{
name|ElementType
operator|.
name|PARAMETER
block|}
argument_list|)
DECL|annotation|HasAnnotation
specifier|public
annotation_defn|@interface
name|HasAnnotation
block|{
DECL|method|value ()
name|Class
name|value
parameter_list|()
function_decl|;
block|}
end_annotation_defn

end_unit

